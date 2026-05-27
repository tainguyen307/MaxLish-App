package com.example.maxlish.data.model

data class User(

    // Auth
    val uid: String = "",
    val email: String = "",

    // Profile
    val displayName: String = "",
    val avatarUrl: String = "",

    // Learning
    val learningGoal: LearningGoal = LearningGoal.IELTS,
    val level: UserLevel = UserLevel.A1,

    // Daily Target
    val dailyNewWords: Int = 20,
    val dailyReviewWords: Int = 50,

    // Statistics
    val streak: Int = 0,
    val totalWordsLearned: Int = 0,

    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,

    // Gamification
    val xp: Int = 0,

    // Notification
    val notificationEnabled: Boolean = true,

    // System
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)