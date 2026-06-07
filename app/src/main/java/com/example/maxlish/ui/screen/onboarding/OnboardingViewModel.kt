package com.example.maxlish.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.repository.AuthRepository
import com.example.maxlish.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {
    private val authRepository: AuthRepository = FirebaseAuthRepository()

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.GoalSelected -> _state.update {
                it.copy(selectedGoal = event.goal)
            }
            is OnboardingEvent.LevelSelected -> _state.update {
                it.copy(selectedLevel = event.level)
            }
            is OnboardingEvent.DailyWordsSelected -> _state.update {
                it.copy(dailyWords = event.count)
            }
            OnboardingEvent.NextStep -> {
                val s = _state.value
                when (s.step) {
                    1 -> if (s.selectedGoal == null) return else _state.update { it.copy(step = 2) }
                    2 -> if (s.selectedLevel == null) return else _state.update { it.copy(step = 3) }
                    3 -> finishOnboarding()
                }
            }
            OnboardingEvent.PrevStep -> {
                val cur = _state.value.step
                if (cur > 1) _state.update { it.copy(step = cur - 1) }
            }
            OnboardingEvent.Finish -> finishOnboarding()
        }
    }

    private fun finishOnboarding() {
        val s = _state.value
        val goal = s.selectedGoal ?: return
        val level = s.selectedLevel ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            // Lấy displayName hiện tại để không bị ghi đè khi update
            val currentUser = authRepository.getUserProfile()
            val displayName = currentUser?.displayName ?: ""

            val result = authRepository.updateProfile(
                displayName = displayName,
                learningGoal = goal.name,
                level = level.name,
                dailyNewWords = s.dailyWords
            )

            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isDone = true) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
            )
        }
    }
}
