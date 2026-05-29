package com.example.maxlish.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.StudySession
import com.example.maxlish.data.repository.FirebaseLearningRepository
import com.example.maxlish.data.repository.FirebaseProgressRepository
import com.example.maxlish.data.repository.FirebaseVocabularyRepository
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val firestore =
        FirebaseFirestore.getInstance()

    private val auth =
        FirebaseAuth.getInstance()

    private val vocabularyRepository =
        FirebaseVocabularyRepository(firestore)

    private val progressRepository =
        FirebaseProgressRepository(firestore)

    private val learningRepository =
        FirebaseLearningRepository(firestore)

    private val _state =
        MutableStateFlow(HomeState())

    val state: StateFlow<HomeState> =
        _state.asStateFlow()

    private val _navigation = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val navigation = _navigation.asSharedFlow()

    init {
        observeHomeData()
    }

    private fun observeHomeData() {

        val userId =
            auth.currentUser?.uid ?: return

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {

                // =========================
                // Vocabulary Sets
                // =========================

                val setsResult =
                    vocabularyRepository
                        .getVocabularySets(userId)

                val sets =
                    setsResult.getOrNull()
                        ?: emptyList()

                // =========================
                // Learning Progress
                // =========================

                val progressResult =
                    learningRepository
                        .getLearningProgress(userId)

                val progressList =
                    progressResult.getOrNull()
                        ?: emptyList()

                // =========================
                // Due Reviews
                // =========================

                val dueReviewResult =
                    learningRepository
                        .getDueReviews(userId)

                val dueReviews =
                    dueReviewResult.getOrNull()
                        ?: emptyList()

                // =========================
                // Course Mapping
                // =========================

                val courses =
                    sets.mapIndexed { index, set ->

                        val setProgress =
                            progressList.filter {
                                it.wordId.startsWith(set.setId)
                            }

                        val masteredWords =
                            setProgress.count {
                                it.mastered
                            }

                        val progress =
                            if (set.wordCount == 0) {
                                0f
                            } else {
                                masteredWords.toFloat() /
                                        set.wordCount.toFloat()
                            }

                        VocabularySetUiModel(
                            id = set.setId,

                            title = set.title,

                            totalWords = set.wordCount,

                            progress = progress
                        )
                    }

                // =========================
                // Combine realtime sources
                // =========================

                combine(

                    progressRepository
                        .getUserStats(userId),

                    progressRepository
                        .getStudySessions(userId)

                ) { user, sessions ->

                    Pair(user, sessions)

                }.collect { (user, sessions) ->

                    val currentCourse =
                        courses.firstOrNull()

                    val totalCorrect =
                        progressList.sumOf {
                            it.correctCount
                        }

                    val totalWrong =
                        progressList.sumOf {
                            it.wrongCount
                        }

                    val totalAnswers =
                        totalCorrect + totalWrong

                    val accuracy =
                        if (totalAnswers == 0) {
                            0
                        } else {
                            (totalCorrect * 100) / totalAnswers
                        }

                    val masteredWords =
                        progressList.count {
                            it.mastered
                        }

                    _state.value = _state.value.copy(

                        isLoading = false,

                        userName =
                            user?.displayName
                                ?: "Learner 👋",

                        streak =
                            user?.streak ?: 0,

                        reviewCount =
                            dueReviews.size,

                        currentCourseTitle =
                            currentCourse?.title
                                ?: "Start Learning",

                        currentLesson =
                            "${currentCourse?.totalWords ?: 0} words",

                        remainingWords =
                            (currentCourse?.totalWords ?: 0) -
                                    (currentCourse?.progress
                                        ?.times(
                                            currentCourse.totalWords
                                        )
                                        ?.toInt() ?: 0),

                        learningProgress =
                            currentCourse?.progress ?: 0f,

                        earnedXp =
                            masteredWords * 10,

                        accuracy =
                            accuracy,

                        weeklyActivity =
                            buildWeeklyActivity(
                                sessions
                            ),

                        vocabularySets = courses
                    )
                }

            } catch (e: Exception) {

                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    private fun buildWeeklyActivity(
        sessions: List<StudySession>
    ): List<Int> {

        val result =
            MutableList(7) { 0 }

        sessions
            .takeLast(7)
            .forEachIndexed { index, session ->

                result[index] =
                    session.durationMinutes
                        .coerceAtMost(100)
            }

        return result
    }

    fun onEvent(event: HomeEvent) {

        when (event) {

            is HomeEvent.OnContinueLearningClick -> {

            }

            is HomeEvent.OnVocabularySetClick -> {
                viewModelScope.launch {
                    _navigation.emit(
                        "vocabulary_set_detail/${event.setId}"
                    )
                }
            }

            is HomeEvent.OnReviewClick -> {

            }

            is HomeEvent.OnProgressClick -> {

            }

            is HomeEvent.OnDailyChallengeClick -> {

            }

            else -> {

            }
        }
    }
}