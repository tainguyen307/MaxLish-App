package com.example.maxlish.data.model

import kotlin.math.ceil

object Sm2Algorithm {
    fun calculate(
        quality: ReviewQuality,
        previousRepetition: Int,
        previousInterval: Int,
        previousEaseFactor: Double
    ): Sm2Result {
        // Map ReviewQuality to SM-2 quality (0 to 5)
        // AGAIN -> 2 (Incorrect response; correct one easily remembered)
        // HARD  -> 3 (Correct response recalled with serious difficulty)
        // GOOD  -> 4 (Correct response after a hesitation)
        // EASY  -> 5 (Perfect response)
        val q = when (quality) {
            ReviewQuality.AGAIN -> 2
            ReviewQuality.HARD -> 3
            ReviewQuality.GOOD -> 4
            ReviewQuality.EASY -> 5
        }

        var repetition = previousRepetition
        var interval: Int
        var easeFactor = previousEaseFactor

        // Calculate easeFactor
        easeFactor = easeFactor + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
        if (easeFactor < 1.3) {
            easeFactor = 1.3
        }

        if (q >= 3) {
            repetition += 1
            interval = when (repetition) {
                1 -> 1
                2 -> 6
                else -> ceil(previousInterval * easeFactor).toInt()
            }
        } else {
            // q < 3 (Again / incorrect response)
            repetition = 0
            interval = 1
        }

        return Sm2Result(
            repetition = repetition,
            interval = interval,
            easeFactor = easeFactor,
            nextReviewAt = System.currentTimeMillis() + interval * 86400000L // interval * 24h
        )
    }
}

data class Sm2Result(
    val repetition: Int,
    val interval: Int,
    val easeFactor: Double,
    val nextReviewAt: Long
)
