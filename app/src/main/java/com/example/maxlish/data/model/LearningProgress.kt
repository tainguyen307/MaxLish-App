package com.example.maxlish.data.model

data class LearningProgress(

    val progressId: String = "",

    val userId: String = "",
    val setId: String = "",
    val wordId: String = "",

    // SM-2
    val repetition: Int = 0,
    val interval: Int = 1,
    val easeFactor: Double = 2.5,

    // Schedule
    val lastReviewedAt: Long = 0L,
    val nextReviewAt: Long = 0L,

    // Statistics
    val correctCount: Int = 0,
    val wrongCount: Int = 0,

    // State
    val mastered: Boolean = false,

    // System
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)