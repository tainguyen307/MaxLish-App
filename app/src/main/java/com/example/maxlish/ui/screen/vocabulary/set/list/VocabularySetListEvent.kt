package com.example.maxlish.ui.screen.vocabulary.set.list

sealed class VocabularySetListEvent {
    data class OnSetClick(val setId: String) : VocabularySetListEvent()
    data class OnSearchChange(val query: String) : VocabularySetListEvent()
    data object OnCreateSetClick : VocabularySetListEvent()

    data class OnLearnClick(val setId: String) : VocabularySetListEvent()
    data class OnEditClick(val setId: String) : VocabularySetListEvent()
    data class OnDeleteClick(val setId: String) : VocabularySetListEvent()
    data object OnRetry : VocabularySetListEvent()
}