package com.example.maxlish.ui.screen.home

import androidx.lifecycle.ViewModel
import com.example.maxlish.ui.screen.home.model.CourseUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        HomeState(
            courses = listOf(
                CourseUiModel(
                    id = "1",
                    title = "IELTS Vocabulary",
                    totalWords = 840,
                    progress = 0.75f
                ),
                CourseUiModel(
                    id = "2",
                    title = "Daily Communication",
                    totalWords = 250,
                    progress = 0.35f
                ),
                CourseUiModel(
                    id = "3",
                    title = "Business English",
                    totalWords = 1200,
                    progress = 0.15f
                )
            )
        )
    )

    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun onEvent(
        event: HomeEvent
    ) {

        when (event) {

            HomeEvent.OnProfileClick -> {

            }

            HomeEvent.OnReviewClick -> {

            }

            HomeEvent.OnContinueLearningClick -> {

            }

            HomeEvent.OnDailyChallengeClick -> {

            }

            is HomeEvent.OnCourseClick -> {

            }

            HomeEvent.OnProgressClick -> {
                // Handle progress click if needed, or leave for Route
            }
        }
    }
}