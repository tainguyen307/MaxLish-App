package com.example.maxlish.ui.screen.vocabulary.word.detail

import com.example.maxlish.data.model.VocabularyWord

data class VocabularyWordDetailState(

    val isLoading: Boolean = false,

    val word: VocabularyWord? = null,

    val errorMessage: String? = null
)