package com.example.maxlish.ui.screen.register

sealed class RegisterEvent {
    data class NameChanged(val value: String) : RegisterEvent()
    data class EmailChanged(val value: String) : RegisterEvent()
    data class PasswordChanged(val value: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val value: String) : RegisterEvent()
    object RegisterClicked : RegisterEvent()
}
