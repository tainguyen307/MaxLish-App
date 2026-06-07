package com.example.maxlish.ui.screen.vocabulary.set.create
sealed interface VocabularySetCreateEvent {

    data class OnTitleChange(val value: String) : VocabularySetCreateEvent

    data class OnDescriptionChange(val value: String) : VocabularySetCreateEvent

    data class OnTagInputChange(val value: String) : VocabularySetCreateEvent

    data object OnAddTag : VocabularySetCreateEvent

    data class OnRemoveTag(val tag: String) : VocabularySetCreateEvent

    data object OnCreateClick : VocabularySetCreateEvent

    data object OnBackClick : VocabularySetCreateEvent
}