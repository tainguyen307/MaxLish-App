package com.example.maxlish.ui.screen.learning

import com.example.maxlish.data.model.ReviewQuality

sealed class LearningEvent {
    data object FlipCard : LearningEvent()
    data class RateCard(val quality: ReviewQuality) : LearningEvent()
    data object FinishSession : LearningEvent()
}
