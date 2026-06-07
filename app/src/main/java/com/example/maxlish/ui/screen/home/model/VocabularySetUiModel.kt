package com.example.maxlish.ui.screen.home.model

data class VocabularySetUiModel(

    val id: String,

    val title: String,

    val totalWords: Int,

    val learnedWords: Int = 0,

    val progress: Float
)
