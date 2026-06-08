package com.example.maxlish.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.repository.FirebaseAuthRepository
import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.UserLevel
import com.example.maxlish.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val user = authRepository.getUserProfile()
            if (user != null) {
                val goalEnum = try {
                    LearningGoal.valueOf(user.learningGoal)
                } catch (e: Exception) {
                    LearningGoal.IELTS
                }
                val levelEnum = try {
                    UserLevel.valueOf(user.level)
                } catch (e: Exception) {
                    UserLevel.A1
                }
                _state.update {
                    it.copy(
                        displayName = user.displayName,
                        learningGoal = goalEnum,
                        level = levelEnum,
                        isLoading = false
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.NameChanged -> _state.update {
                it.copy(displayName = event.value, errorMessage = null)
            }
            is ProfileEvent.GoalChanged -> _state.update {
                it.copy(learningGoal = event.value)
            }
            is ProfileEvent.LevelChanged -> _state.update {
                it.copy(level = event.value)
            }
            ProfileEvent.SaveClicked -> saveProfile()
            ProfileEvent.LogoutClicked -> logout()
            ProfileEvent.SaveSuccessConsumed -> _state.update {
                it.copy(isSaveSuccess = false)
            }
        }
    }

    private fun saveProfile() {
        val s = _state.value
        if (s.displayName.isBlank()) {
            _state.update { it.copy(errorMessage = "Tên không được để trống") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.updateProfile(
                displayName = s.displayName,
                learningGoal = s.learningGoal.name,
                level = s.level.name
                // dailyNewWords uses default = 20 — ProfileScreen không chỉnh field này
            )
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isSaveSuccess = true) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
