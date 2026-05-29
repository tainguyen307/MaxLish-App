package com.example.maxlish.ui.screen.vocabulary.set.list

sealed interface VocabularySetListEvent {

    data class OnSearchChange(
        val query: String
    ) : VocabularySetListEvent

    data class OnSetClick(
        val setId: String
    ) : VocabularySetListEvent

    data object OnCreateSetClick :
        VocabularySetListEvent
}