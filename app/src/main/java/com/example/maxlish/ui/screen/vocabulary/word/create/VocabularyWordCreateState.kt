package com.example.maxlish.ui.screen.vocabulary.word.create

data class VocabularyWordCreateState(

    val isLoading: Boolean = false,

    val isEditMode: Boolean = false,

    val wordId: String? = null,

    val setId: String = "",

    val word: String = "",
    val pronunciation: String = "",
    val meaning: String = "",
    val description: String = "",
    val example: String = "",

    val difficulty: String = "Easy",
    val collocations: List<String> = emptyList(),
    val relatedWords: List<String> = emptyList(),
    val note: String = "",

    val collocationInput: String = "",
    val relatedWordInput: String = "",

    val errorMessage: String? = null,

    val success: Boolean = false
)