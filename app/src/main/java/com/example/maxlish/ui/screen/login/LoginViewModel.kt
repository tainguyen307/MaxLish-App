package com.example.maxlish.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.repository.AuthRepository
import com.example.maxlish.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update {
                    it.copy(
                        email = event.value,
                        emailError = null,
                        generalError = null
                    )
                }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update {
                    it.copy(
                        password = event.value,
                        passwordError = null,
                        generalError = null
                    )
                }
            }
            LoginEvent.LoginClicked -> {
                if (validateLogin()) {
                    performLogin()
                }
            }
            LoginEvent.GoogleSignInClicked -> {
                // Được handle ở Activity/Screen thông qua Google Sign-In flow
            }
            is LoginEvent.GoogleIdTokenReceived -> {
                loginWithGoogle(event.idToken)
            }
        }
    }

    private fun validateLogin(): Boolean {
        val currentState = _state.value
        var hasError = false
        var emailError: String? = null
        var passwordError: String? = null

        if (currentState.email.isBlank()) {
            emailError = "Email không được để trống"
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            emailError = "Địa chỉ email không hợp lệ"
            hasError = true
        }

        if (currentState.password.length < 6) {
            passwordError = "Mật khẩu phải có ít nhất 6 ký tự"
            hasError = true
        }

        _state.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError
            )
        }
        return !hasError
    }

    private fun performLogin() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalError = null) }
            val result = authRepository.login(currentState.email, currentState.password)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            generalError = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalError = null) }
            val result = authRepository.loginWithGoogle(idToken)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            generalError = error.message
                        )
                    }
                }
            )
        }
    }
}