package com.example.maxlish.ui.screen.profile

sealed class ProfileEvent {
    data class NameChanged(val value: String) : ProfileEvent()
    data class GoalChanged(val value: String) : ProfileEvent()
    data class LevelChanged(val value: String) : ProfileEvent()
    object SaveClicked : ProfileEvent()
    object LogoutClicked : ProfileEvent()
}
