package com.example.maxlish.ui.screen.vocabulary.set.detail

import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel

data class VocabularySetDetailState(
    val isLoading: Boolean = false,
    val vocabularySet: VocabularySetUiModel? = null,
    val errorMessage: String? = null
)