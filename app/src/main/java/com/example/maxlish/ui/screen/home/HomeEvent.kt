package com.example.maxlish.ui.screen.home

sealed interface HomeEvent {

    data object OnProfileClick : HomeEvent

    data object OnReviewClick : HomeEvent

    data object OnContinueLearningClick : HomeEvent

    data object OnDailyChallengeClick : HomeEvent

    data class OnCourseClick(
        val courseId: String
    ) : HomeEvent
}