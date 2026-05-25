package com.example.maxlish.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val learningGoal: String = "",   // IELTS, TOEIC, Giao tiếp, Khác
    val level: String = "A1"         // A1, A2, B1, B2, C1, C2
)
