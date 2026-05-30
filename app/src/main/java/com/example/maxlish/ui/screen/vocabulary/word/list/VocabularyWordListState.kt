package com.example.maxlish.ui.screen.vocabulary.word.list

import com.example.maxlish.data.model.VocabularyWord

data class VocabularyWordListState(

    val isLoading: Boolean = false,

    val words: List<VocabularyWord> = emptyList(),

    val searchQuery: String = "",

    val errorMessage: String? = null
)