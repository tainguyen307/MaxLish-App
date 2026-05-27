package com.example.maxlish.ui.screen.profile

import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.UserLevel

sealed class ProfileEvent {
    data class NameChanged(val value: String) : ProfileEvent()
    data class GoalChanged(val value: LearningGoal) : ProfileEvent()
    data class LevelChanged(val value: UserLevel) : ProfileEvent()
    object SaveClicked : ProfileEvent()
    object LogoutClicked : ProfileEvent()
}
