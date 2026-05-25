package com.example.maxlish.ui.screen.profile

data class ProfileState(
    val displayName: String = "",
    val learningGoal: String = "IELTS",
    val level: String = "A1",
    val isLoading: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val errorMessage: String? = null
)

val LEARNING_GOALS = listOf("IELTS", "TOEIC", "Giao tiếp", "Khác")
val LEVELS = listOf("A1", "A2", "B1", "B2", "C1", "C2")
