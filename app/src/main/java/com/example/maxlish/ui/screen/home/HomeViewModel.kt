package com.example.maxlish.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.StudySession
import com.example.maxlish.data.repository.FirebaseLearningRepository
import com.example.maxlish.data.repository.FirebaseProgressRepository
import com.example.maxlish.data.repository.FirebaseVocabularyRepository
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
            // 1. REALTIME VOCAB SETS
            // =========================
            viewModelScope.launch {
                vocabularyRepository.observeVocabularySets(userId)
                    .collect { sets ->
                        setsFlow.value = sets
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
                progressRepository.getStudySessions(userId)
            ) { sets, user, sessions ->

                val progressList =
                    learningRepository.getLearningProgress(userId)
                        .getOrNull() ?: emptyList()

                val courses =
                    sets.map { set ->

                        val setProgress =
                            progressList.filter {
                                it.wordId.startsWith(set.setId)
                            }

                        val masteredWords =
                            setProgress.count { it.mastered }

                        val progress =
                            if (set.wordCount == 0) 0f
                            else masteredWords.toFloat() / set.wordCount.toFloat()

                        VocabularySetUiModel(
                            id = set.setId,
                            title = set.title,
                            totalWords = set.wordCount,
                            progress = progress
                        )
                    }

                Triple(user, sessions, courses)
            }
                .catch { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collect { (user, sessions, courses) ->

                    val currentCourse = courses.firstOrNull()

                    val progressList =
                        learningRepository.getLearningProgress(userId)
                            .getOrNull() ?: emptyList()

                    val totalCorrect =
                        progressList.sumOf { it.correctCount }

                    val totalWrong =
                        progressList.sumOf { it.wrongCount }

                    val totalAnswers = totalCorrect + totalWrong

                    val accuracy =
                        if (totalAnswers == 0) 0
                        else (totalCorrect * 100) / totalAnswers

                    val masteredWords =
                        progressList.count { it.mastered }

                    _state.value = _state.value.copy(

                        isLoading = false,

                        userName = user?.displayName ?: "Learner 👋",
                        streak = user?.streak ?: 0,

                        reviewCount = 0, // nếu bạn chưa làm flow dueReviews

                        currentCourseTitle =
                            currentCourse?.title ?: "Start Learning",

                        currentLesson =
                            "${currentCourse?.totalWords ?: 0} words",

                        remainingWords =
                            (currentCourse?.totalWords ?: 0) -
                                    ((currentCourse?.progress ?: 0f)
                                            * (currentCourse?.totalWords ?: 0)).toInt(),

                        learningProgress = currentCourse?.progress ?: 0f,

                        earnedXp = masteredWords * 10,

                        accuracy = accuracy,

                        weeklyActivity = buildWeeklyActivity(sessions),

                        vocabularySets = courses
                    )
                }
        }
    }

    private fun buildWeeklyActivity(
        sessions: List<StudySession>
    ): List<Int> {

        val result = MutableList(7) { 0 }

        sessions.takeLast(7).forEachIndexed { index, session ->
            result[index] = session.durationMinutes.coerceAtMost(100)
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