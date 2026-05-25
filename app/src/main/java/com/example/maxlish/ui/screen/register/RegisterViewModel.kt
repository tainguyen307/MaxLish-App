package com.example.maxlish.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.R
import com.example.maxlish.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NameChanged -> _state.update {
                it.copy(name = event.value, nameError = null, generalError = null)
            }
            is RegisterEvent.EmailChanged -> _state.update {
                it.copy(email = event.value, emailError = null, generalError = null)
            }
            is RegisterEvent.PasswordChanged -> _state.update {
                it.copy(password = event.value, passwordError = null, generalError = null)
            }
            is RegisterEvent.ConfirmPasswordChanged -> _state.update {
                it.copy(confirmPassword = event.value, confirmPasswordError = null, generalError = null)
            }
            RegisterEvent.RegisterClicked -> {
                if (validate()) performRegister()
            }
        }
    }

    private fun validate(): Boolean {
        val s = _state.value
        var nameError: Int? = null
        var emailError: Int? = null
        var passwordError: Int? = null
        var confirmPasswordError: Int? = null
        var hasError = false

        if (s.name.isBlank()) {
            nameError = R.string.error_empty_name
            hasError = true
        }
        if (s.email.isBlank()) {
            emailError = R.string.error_empty_email
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) {
            emailError = R.string.error_invalid_email
            hasError = true
        }
        if (s.password.length < 6) {
            passwordError = R.string.error_invalid_password
            hasError = true
        }
        if (s.confirmPassword != s.password) {
            confirmPasswordError = R.string.error_mismatched_confirm_password
            hasError = true
        }

        _state.update {
            it.copy(
                nameError = nameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }
        return !hasError
    }

    private fun performRegister() {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalError = null) }
            val result = authRepository.register(s.email, s.password, s.name)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, generalError = error.message)
                    }
                }
            )
        }
    }
}
