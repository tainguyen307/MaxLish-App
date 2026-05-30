package com.example.maxlish.ui.screen.vocabulary.word.create

import com.example.maxlish.data.model.VocabularyWord

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

    val errorMessage: String? = null,

    val success: Boolean = false
)