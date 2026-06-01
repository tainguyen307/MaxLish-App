package com.example.maxlish.ui.screen.vocabulary.word.create

sealed class VocabularyWordCreateEvent {

    data class OnWordChange(val value: String) :
        VocabularyWordCreateEvent()

    data class OnMeaningChange(val value: String) :
        VocabularyWordCreateEvent()

    data class OnPronunciationChange(val value: String) :
        VocabularyWordCreateEvent()

    data class OnDescriptionChange(val value: String) :
        VocabularyWordCreateEvent()

    data class OnExampleChange(val value: String) :
        VocabularyWordCreateEvent()

    data object OnSaveClick :
        VocabularyWordCreateEvent()
}