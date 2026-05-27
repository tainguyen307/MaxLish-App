package com.example.maxlish.ui.screen.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.StudySession
import com.example.maxlish.data.model.User
import com.example.maxlish.data.repository.AuthRepository
import com.example.maxlish.data.repository.ProgressRepository
import kotlinx.coroutines.flow.*

data class ProgressUiState(
    val user: User? = null,
    val studySessions: List<StudySession> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProgressViewModel(
    private val authRepository: AuthRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgressData()
    }

    private fun loadProgressData() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser == null) {
            _uiState.update { it.copy(error = "Người dùng chưa đăng nhập") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        combine(
            progressRepository.getUserStats(currentUser.uid),
            progressRepository.getStudySessions(currentUser.uid)
        ) { user, sessions ->
            ProgressUiState(
                user = user ?: User(uid = currentUser.uid, email = currentUser.email, displayName = currentUser.displayName),
                studySessions = sessions,
                isLoading = false
            )
        }.onEach { state ->
            _uiState.value = state
        }.catch { e ->
            _uiState.update { it.copy(isLoading = false, error = e.message ?: "Đã có lỗi xảy ra") }
        }.launchIn(viewModelScope)
    }

    fun getAccuracy(): Float {
        val user = _uiState.value.user ?: return 0f
        val total = user.correctAnswers + user.wrongAnswers
        if (total == 0) return 0f
        return (user.correctAnswers.toFloat() / total) * 100
    }

    fun getLevelCategory(): String {
        val user = _uiState.value.user ?: return "Beginner"
        return when (user.level.name) {
            "A1", "A2" -> "Beginner"
            "B1", "B2" -> "Intermediate"
            "C1", "C2" -> "Advanced"
            else -> "Beginner"
        }
    }
}
