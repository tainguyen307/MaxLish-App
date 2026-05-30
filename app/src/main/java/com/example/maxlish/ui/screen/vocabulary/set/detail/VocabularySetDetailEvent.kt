package com.example.maxlish.ui.screen.vocabulary.set.detail

sealed class VocabularySetDetailEvent {

    data object OnBackClick : VocabularySetDetailEvent()

    data object OnLearnClick : VocabularySetDetailEvent()

    data object OnViewWordsClick : VocabularySetDetailEvent()

    data object OnAddWordClick : VocabularySetDetailEvent()
}