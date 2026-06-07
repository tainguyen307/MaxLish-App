package com.example.maxlish.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.StudySession
import com.example.maxlish.data.repository.FirebaseLearningRepository
import com.example.maxlish.data.repository.FirebaseProgressRepository
import com.example.maxlish.data.repository.FirebaseVocabularyRepository
import com.example.maxlish.data.seed.SeedData
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val vocabularyRepository =
        FirebaseVocabularyRepository(firestore)

    private val progressRepository =
        FirebaseProgressRepository(firestore)

    private val learningRepository =
        FirebaseLearningRepository(firestore)

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(0)

    fun refresh() {
        _refreshTrigger.value++
    }

    private val _navigation = MutableSharedFlow<String>()
    val navigation = _navigation.asSharedFlow()

    // =========================
    // REACTIVE SOURCE (IMPORTANT)
    // =========================
    private val setsFlow =
        MutableStateFlow<List<com.example.maxlish.data.model.VocabularySet>>(emptyList())

    init {

        val userId = auth.currentUser?.uid ?: ""

        if (userId.isNotBlank()) {

            // =========================
            // 1. REALTIME VOCAB SETS (TỰ ĐỘNG SEED KHI RỖNG)
            // =========================
            viewModelScope.launch {
                vocabularyRepository.observeVocabularySets(userId)
                    .collect { sets ->
                        if (sets.isEmpty()) {
                            try {
                                val email = auth.currentUser?.email ?: "newuser@example.com"
                                Log.d("AUTO_SEED", "Chưa có dữ liệu. Tiến hành tự động nạp dữ liệu mẫu cho $userId...")
                                SeedData().seedForUser(userId, email)
                            } catch (e: Exception) {
                                Log.e("AUTO_SEED", "Lỗi nạp dữ liệu tự động: ${e.message}")
                            }
                        } else {
                            setsFlow.value = sets
                        }
                    }
            }

            // =========================
            // 2. REFRESH TRIGGER (GIỮ NGUYÊN LOGIC CŨ)
            // =========================
            viewModelScope.launch {
                _refreshTrigger.collect {
                    observeHomeData(userId)
                }
            }
        }
    }

    private fun observeHomeData(userId: String) {

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            combine(
                setsFlow,
                progressRepository.getUserStats(userId),
                progressRepository.getStudySessions(userId),
                learningRepository.observeLearningProgress(userId)
            ) { sets, user, sessions, progressList ->

                val dueReviews =
                    learningRepository.getDueReviews(userId)
                        .getOrNull() ?: emptyList()

                // Build vocabulary set UI models with REAL progress data
                // Progress is based on how many words have LearningProgress records
                // (i.e., words that the user has studied at least once)
                val courses =
                    sets.map { set ->

                        val setProgress =
                            progressList.filter {
                                it.setId == set.setId
                            }

                        // Count words that have been studied (have a LearningProgress entry)
                        val learnedWords = setProgress.size

                        // Count words that are fully mastered (SM-2 mastered flag)
                        val masteredWords =
                            setProgress.count { it.mastered }

                        // Progress is based on learned words, not just mastered
                        // This gives users immediate visual feedback after studying
                        val progress =
                            if (set.wordCount == 0) 0f
                            else learnedWords.toFloat() / set.wordCount.toFloat()

                        VocabularySetUiModel(
                            id = set.setId,
                            title = set.title,
                            totalWords = set.wordCount,
                            learnedWords = learnedWords,
                            progress = progress.coerceAtMost(1f)
                        )
                    }

                // Compute global stats from User profile for consistency
                val totalCorrect = user?.correctAnswers ?: 0
                val totalWrong = user?.wrongAnswers ?: 0

                val totalAnswers = totalCorrect + totalWrong

                val accuracy =
                    if (totalAnswers == 0) 0
                    else (totalCorrect * 100) / totalAnswers

                // Total words learned across all sets
                val totalLearnedWords = progressList.size

                // Total mastered words
                val totalMasteredWords = progressList.count { it.mastered }

                // Pack all computed data into a single result
                data class HomeData(
                    val user: com.example.maxlish.data.model.User?,
                    val sessions: List<StudySession>,
                    val courses: List<VocabularySetUiModel>,
                    val dueReviewCount: Int,
                    val accuracy: Int,
                    val totalLearnedWords: Int,
                    val totalMasteredWords: Int
                )

                HomeData(
                    user = user,
                    sessions = sessions,
                    courses = courses,
                    dueReviewCount = dueReviews.size,
                    accuracy = accuracy,
                    totalLearnedWords = totalLearnedWords,
                    totalMasteredWords = totalMasteredWords
                )
            }
                .catch { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collect { data ->

                    val currentCourse = data.courses.firstOrNull()

                    // Remaining words = total words in current set - learned words in current set
                    val remaining = if (currentCourse != null) {
                        (currentCourse.totalWords - currentCourse.learnedWords).coerceAtLeast(0)
                    } else 0

                     _state.value = _state.value.copy(

                         isLoading = false,

                         userName = data.user?.displayName ?: "Learner 👋",
                         streak = data.user?.streak ?: 0,

                         reviewCount = data.dueReviewCount,

                         currentCourseTitle =
                             currentCourse?.title ?: "Start Learning",

                         currentLesson =
                             "${currentCourse?.totalWords ?: 0} words",

                         remainingWords = remaining,

                         learningProgress = currentCourse?.progress ?: 0f,

                         earnedXp = data.totalMasteredWords * 10,

                         accuracy = data.accuracy,

                         weeklyActivity = buildWeeklyActivity(data.sessions),

                         vocabularySets = data.courses,
                         currentVocabularySetId = currentCourse?.id,
                         currentVocabularySetTitle = currentCourse?.title ?: "Start Learning"
                     )
                }
        }
    }

    private fun buildWeeklyActivity(
        sessions: List<StudySession>
    ): List<Int> {

        val result = MutableList(7) { 0 }

        // Only include sessions from the last 7 days
        val sevenDaysAgo = System.currentTimeMillis() - 7 * 86400000L

        sessions
            .filter { it.startedAt >= sevenDaysAgo }
            .forEach { session ->
                val calendar = java.util.Calendar.getInstance()
                calendar.timeInMillis = session.startedAt
                val dayIndex = when (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
                    java.util.Calendar.MONDAY -> 0
                    java.util.Calendar.TUESDAY -> 1
                    java.util.Calendar.WEDNESDAY -> 2
                    java.util.Calendar.THURSDAY -> 3
                    java.util.Calendar.FRIDAY -> 4
                    java.util.Calendar.SATURDAY -> 5
                    java.util.Calendar.SUNDAY -> 6
                    else -> 0
                }
                result[dayIndex] += session.durationMinutes.coerceAtMost(100)
            }

        return result
    }

    fun onEvent(event: HomeEvent) {
        when (event) {

            is HomeEvent.OnVocabularySetClick -> {
                viewModelScope.launch {
                    _navigation.emit("vocabulary_set_detail/${event.setId}")
                }
            }

            else -> Unit
        }
    }
}