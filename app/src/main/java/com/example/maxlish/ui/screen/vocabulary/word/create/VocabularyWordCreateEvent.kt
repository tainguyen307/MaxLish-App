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

    data class OnDifficultyChange(val value: String) :
        VocabularyWordCreateEvent()

    data class OnNoteChange(val value: String) :
        VocabularyWordCreateEvent()

    data class OnCollocationInputChange(val value: String) :
        VocabularyWordCreateEvent()

    data object OnAddCollocation :
        VocabularyWordCreateEvent()

    data class OnRemoveCollocation(val value: String) :
        VocabularyWordCreateEvent()

    data class OnRelatedWordInputChange(val value: String) :
        VocabularyWordCreateEvent()

    data object OnAddRelatedWord :
        VocabularyWordCreateEvent()

    data class OnRemoveRelatedWord(val value: String) :
        VocabularyWordCreateEvent()

    data object OnBackClick :
        VocabularyWordCreateEvent()

    data object OnSaveClick :
        VocabularyWordCreateEvent()
}