package com.example.maxlish.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val user = authRepository.getCurrentUser()
        if (user != null) {
            _state.update {
                it.copy(displayName = user.displayName)
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
            val result = authRepository.updateProfile(s.displayName, s.learningGoal.name, s.level.name)
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
