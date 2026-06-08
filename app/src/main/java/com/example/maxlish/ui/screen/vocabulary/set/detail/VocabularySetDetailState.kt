package com.example.maxlish.ui.screen.vocabulary.set.detail

import com.example.maxlish.data.model.VocabularySet

data class VocabularySetDetailState(
    val isLoading: Boolean = false,
    val vocabularySet: VocabularySet? = null,
    val totalWords: Int = 0,
    val errorMessage: String? = null
)