package com.example.maxlish.ui.screen.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.StudySession
import com.example.maxlish.data.model.User
import com.example.maxlish.data.repository.AuthRepository
import com.example.maxlish.data.repository.ProgressRepository
import com.example.maxlish.data.repository.LearningRepository
import kotlinx.coroutines.flow.*

data class ProgressUiState(
    val user: User? = null,
    val studySessions: List<StudySession> = emptyList(),
    val weeklyActivity: List<Int> = List(7) { 0 },
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProgressViewModel(
    private val authRepository: AuthRepository,
    private val progressRepository: ProgressRepository,
    private val learningRepository: LearningRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ProgressUiState())

    val uiState: StateFlow<ProgressUiState> =
        _uiState.asStateFlow()

    init {
        loadProgressData()
    }

    private fun loadProgressData() {

        val currentUser =
            authRepository.getCurrentUser()

        if (currentUser == null) {

            _uiState.update {
                it.copy(
                    error = "Người dùng chưa đăng nhập"
                )
            }

            return
        }

        _uiState.update {
            it.copy(isLoading = true)
        }

        combine(
            progressRepository.getUserStats(
                currentUser.uid
            ),
            progressRepository.getStudySessions(
                currentUser.uid
            ),
            learningRepository.observeLearningProgress(
                currentUser.uid
            )
        ) { user, sessions, progressList ->

            val actualLearnedWords = progressList.size
            val updatedUser = user?.copy(totalWordsLearned = actualLearnedWords) ?: User(
                uid = currentUser.uid,
                email = currentUser.email,
                displayName = currentUser.displayName,
                totalWordsLearned = actualLearnedWords
            )

            ProgressUiState(
                user = updatedUser,
                studySessions = sessions,
                weeklyActivity =
                    buildWeeklyActivity(
                        sessions
                    ),
                isLoading = false
            )
        }
            .onEach { state ->
                _uiState.value = state
            }
            .catch { e ->

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                            ?: "Đã có lỗi xảy ra"
                    )
                }
            }
            .launchIn(viewModelScope)
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

            val dayIndex = when (
                calendar.get(java.util.Calendar.DAY_OF_WEEK)
            ) {
                java.util.Calendar.MONDAY -> 0
                java.util.Calendar.TUESDAY -> 1
                java.util.Calendar.WEDNESDAY -> 2
                java.util.Calendar.THURSDAY -> 3
                java.util.Calendar.FRIDAY -> 4
                java.util.Calendar.SATURDAY -> 5
                java.util.Calendar.SUNDAY -> 6
                else -> 0
            }

            result[dayIndex] += session.reviewedWords
        }

        return result
    }

    fun getAccuracy(): Float {

        val user =
            _uiState.value.user ?: return 0f

        val total =
            user.correctAnswers +
                    user.wrongAnswers

        if (total == 0) return 0f

        return (
                user.correctAnswers.toFloat()
                        / total
                ) * 100
    }

    fun getLevelCategory(): String {

        val user =
            _uiState.value.user
                ?: return "Beginner"

        return when (user.level.name) {

            "A1", "A2" ->
                "Beginner"

            "B1", "B2" ->
                "Intermediate"

            "C1", "C2" ->
                "Advanced"

            else ->
                "Beginner"
        }
    }
}