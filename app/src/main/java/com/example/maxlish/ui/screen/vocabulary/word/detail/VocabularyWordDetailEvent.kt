package com.example.maxlish.ui.screen.vocabulary.word.detail

sealed class VocabularyWordDetailEvent {

    data object OnBackClick : VocabularyWordDetailEvent()

    data object OnEditClick : VocabularyWordDetailEvent()

    data object OnDeleteClick : VocabularyWordDetailEvent()

    data object OnRefresh : VocabularyWordDetailEvent()
}