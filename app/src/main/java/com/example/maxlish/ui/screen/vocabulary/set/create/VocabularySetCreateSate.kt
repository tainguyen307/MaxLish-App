package com.example.maxlish.ui.screen.vocabulary.set.create

data class VocabularySetCreateState(
    val title: String = "",
    val description: String = "",
    val selectedTags: List<String> = emptyList(),
    val tagInput: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val isEditMode: Boolean? = false
)