package com.example.maxlish.ui.screen.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        LoginState()
    )
    val state = _state.asStateFlow()
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update {
                    it.copy(
                        email = event.value,
                        emailError = null
                    )
                }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update {
                    it.copy(
                        password = event.value,
                        passwordError = null
                    )
                }
            }
            LoginEvent.LoginClicked -> {
                validateLogin()
            }
        }
    }
    private fun validateLogin() {
        val currentState = _state.value
        var hasError = false
        var emailError: String? = null
        var passwordError: String? = null
        if (
            currentState.email.isBlank()
        ) {
            emailError = "Email cannot be empty"
            hasError = true
        }
        if (
            currentState.password.length < 6
        ) {
            passwordError =
                "Password must be at least 6 characters"
            hasError = true
        }
        _state.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError
            )
        }
        if (!hasError) {
            login()
        }
    }
    private fun login() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        /*
            TODO:
            Call API here later
         */

        _state.update {

            it.copy(
                isLoading = false
            )
        }
    }
}