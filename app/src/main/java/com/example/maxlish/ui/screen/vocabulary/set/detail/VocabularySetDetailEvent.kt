package com.example.maxlish.ui.screen.vocabulary.set.detail

sealed interface VocabularySetDetailEvent {

    data object OnBackClick : VocabularySetDetailEvent

    data object OnStartLearningClick : VocabularySetDetailEvent
}