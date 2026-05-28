package com.example.maxlish.ui.screen.home

import com.example.maxlish.ui.screen.home.model.CourseUiModel

data class HomeState(

    val isLoading: Boolean = false,

    val userName: String = "Tai",

    val streak: Int = 15,

    val reviewCount: Int = 42,

    val todayLearned: Int = 25,

    val todayGoal: Int = 50,

    val courses: List<CourseUiModel> = emptyList(),

    val error: String? = null
)