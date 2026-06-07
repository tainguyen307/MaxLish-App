package com.example.maxlish.ui.screen.onboarding

import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.UserLevel

data class OnboardingState(
    val step: Int = 1,               // 1, 2, 3
    val selectedGoal: LearningGoal? = null,
    val selectedLevel: UserLevel? = null,
    val dailyWords: Int = 20,
    val isLoading: Boolean = false,
    val isDone: Boolean = false,
    val errorMessage: String? = null
)
