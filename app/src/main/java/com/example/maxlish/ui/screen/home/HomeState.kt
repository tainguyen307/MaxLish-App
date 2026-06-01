package com.example.maxlish.ui.screen.home

import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel

data class HomeState(

    val isLoading: Boolean = false,

    val userName: String = "",
    val streak: Int = 0,

    val reviewCount: Int = 0,

    val currentCourseTitle: String = "",
    val currentLesson: String = "",
    val remainingWords: Int = 0,
    val learningProgress: Float = 0f,

    val earnedXp: Int = 0,
    val accuracy: Int = 0,

    val weeklyActivity: List<Int> = emptyList(),

    val errorMessage: String? = null,

    val vocabularySets: List<VocabularySetUiModel> = emptyList(),
    val currentVocabularySetId: String? = null,
    val currentVocabularySetTitle: String = ""
)