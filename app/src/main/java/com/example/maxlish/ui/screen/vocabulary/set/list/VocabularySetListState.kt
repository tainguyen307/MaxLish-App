package com.example.maxlish.ui.screen.vocabulary.set.list

import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel

data class VocabularySetListState(

    val isLoading: Boolean = false,

    val searchQuery: String = "",

    val vocabularySets: List<VocabularySetUiModel> = emptyList(),

    val errorMessage: String? = null
)