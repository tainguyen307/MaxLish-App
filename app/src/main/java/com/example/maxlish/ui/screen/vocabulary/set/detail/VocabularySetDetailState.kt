package com.example.maxlish.ui.screen.vocabulary.set.detail

import com.example.maxlish.data.model.VocabularySet
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel

data class VocabularySetDetailState(
    val isLoading: Boolean = false,
    val vocabularySet: VocabularySet? = null,
    val totalWords: Int = 0,
    val errorMessage: String? = null
)