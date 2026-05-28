package com.example.maxlish.ui.screen.profile

import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.UserLevel

data class ProfileState(
    // Name
    val displayName: String = "",

    // Learning
    val learningGoal: LearningGoal = LearningGoal.IELTS,
    val level: UserLevel = UserLevel.A1,

    // Statistics
    val streak: Int = 0,
    val totalWordsLearned: Int = 0,

    // Gamification
    val xp: Int = 0,

    // General
    val isLoading: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val errorMessage: String? = null
)
