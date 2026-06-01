package com.example.maxlish.ui.screen.vocabulary.word.list

sealed class VocabularyWordListEvent {

    data class OnWordClick(
        val wordId: String
    ) : VocabularyWordListEvent()

    data object OnAddWordClick
        : VocabularyWordListEvent()

    data class OnSearchChange(
        val query: String
    ) : VocabularyWordListEvent()
}