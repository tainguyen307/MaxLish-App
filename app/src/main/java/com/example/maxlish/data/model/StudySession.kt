package com.example.maxlish.data.model

data class StudySession(

    val sessionId: String = "",

    val userId: String = "",

    val reviewedWords: Int = 0,

    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,

    val durationMinutes: Int = 0,

    val startedAt: Long = 0L,
    val endedAt: Long = 0L,

    val createdAt: Long = System.currentTimeMillis()
)