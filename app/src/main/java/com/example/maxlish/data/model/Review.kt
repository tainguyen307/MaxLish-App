package com.example.maxlish.data.model

data class Review(

    val reviewId: String = "",

    val userId: String = "",
    val wordId: String = "",

    // AGAIN HARD GOOD EASY
    val quality: ReviewQuality = ReviewQuality.GOOD,

    // SM-2 Snapshot
    val previousInterval: Int = 0,
    val newInterval: Int = 0,

    val previousEaseFactor: Double = 2.5,
    val newEaseFactor: Double = 2.5,

    val reviewTime: Long = System.currentTimeMillis()
)