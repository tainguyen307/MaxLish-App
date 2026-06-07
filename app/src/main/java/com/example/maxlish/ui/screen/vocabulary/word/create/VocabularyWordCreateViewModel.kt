package com.example.maxlish.ui.screen.vocabulary.word.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.VocabularyWord
import com.example.maxlish.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VocabularyWordCreateViewModel(
    private val repository: VocabularyRepository,
    private val setId: String,
    private val wordId: String? = null
) : ViewModel() {

    private val _state = MutableStateFlow(
        VocabularyWordCreateState(
            setId = setId,
            isEditMode = wordId != null,
            wordId = wordId
        )
    )
    val state: StateFlow<VocabularyWordCreateState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    sealed class Effect {
        object Saved : Effect()
        data class Error(val message: String) : Effect()
    }

    init {
        if (wordId != null) {
            loadWord()
        }
    }

    private fun loadWord() {
        viewModelScope.launch {
            val result = repository.getWordById(setId, wordId!!)

            result.onSuccess { word ->
                _state.update {
                    it.copy(
                        word = word.word,
                        meaning = word.meaning,
                        pronunciation = word.pronunciation,
                        description = word.description,
                        example = word.example,
                        difficulty = word.difficulty,
                        collocations = word.collocations,
                        relatedWords = word.relatedWords,
                        note = word.note
                    )
                }
            }
        }
    }

    fun onEvent(event: VocabularyWordCreateEvent) {
        when (event) {

            is VocabularyWordCreateEvent.OnWordChange ->
                _state.update { it.copy(word = event.value) }

            is VocabularyWordCreateEvent.OnMeaningChange ->
                _state.update { it.copy(meaning = event.value) }

            is VocabularyWordCreateEvent.OnPronunciationChange ->
                _state.update { it.copy(pronunciation = event.value) }

            is VocabularyWordCreateEvent.OnDescriptionChange ->
                _state.update { it.copy(description = event.value) }

            is VocabularyWordCreateEvent.OnExampleChange ->
                _state.update { it.copy(example = event.value) }

            is VocabularyWordCreateEvent.OnDifficultyChange ->
                _state.update { it.copy(difficulty = event.value) }

            is VocabularyWordCreateEvent.OnNoteChange ->
                _state.update { it.copy(note = event.value) }

            is VocabularyWordCreateEvent.OnCollocationInputChange ->
                _state.update { it.copy(collocationInput = event.value) }

            VocabularyWordCreateEvent.OnAddCollocation ->
                _state.update {
                    if (it.collocationInput.isNotBlank() && !it.collocations.contains(it.collocationInput.trim())) {
                        it.copy(
                            collocations = it.collocations + it.collocationInput.trim(),
                            collocationInput = ""
                        )
                    } else {
                        it
                    }
                }

            is VocabularyWordCreateEvent.OnRemoveCollocation ->
                _state.update {
                    it.copy(collocations = it.collocations - event.value)
                }

            is VocabularyWordCreateEvent.OnRelatedWordInputChange ->
                _state.update { it.copy(relatedWordInput = event.value) }

            VocabularyWordCreateEvent.OnAddRelatedWord ->
                _state.update {
                    if (it.relatedWordInput.isNotBlank() && !it.relatedWords.contains(it.relatedWordInput.trim())) {
                        it.copy(
                            relatedWords = it.relatedWords + it.relatedWordInput.trim(),
                            relatedWordInput = ""
                        )
                    } else {
                        it
                    }
                }

            is VocabularyWordCreateEvent.OnRemoveRelatedWord ->
                _state.update {
                    it.copy(relatedWords = it.relatedWords - event.value)
                }

            VocabularyWordCreateEvent.OnBackClick -> Unit

            VocabularyWordCreateEvent.OnSaveClick ->
                saveWord()
        }
    }

    private fun saveWord() {
        val s = _state.value

        if (s.setId.isBlank()) {
            viewModelScope.launch {
                _effect.emit(Effect.Error("setId is empty"))
            }
            return
        }

        if (s.word.isBlank()) {
            viewModelScope.launch {
                _effect.emit(Effect.Error("word is empty"))
            }
            return
        }

        viewModelScope.launch {

            _state.update { it.copy(isLoading = true) }

            val word = VocabularyWord(
                wordId = s.wordId ?: "",
                setId = s.setId,
                word = s.word,
                meaning = s.meaning,
                pronunciation = s.pronunciation,
                description = s.description,
                example = s.example,
                difficulty = s.difficulty,
                collocations = s.collocations,
                relatedWords = s.relatedWords,
                note = s.note
            )

            val result = if (s.isEditMode) {
                repository.updateWord(s.setId, word)
            } else {
                repository.createWord(s.setId, word)
            }

            result
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }

                    _effect.emit(Effect.Saved)
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false) }

                    _effect.emit(
                        Effect.Error(e.message ?: "Unknown error")
                    )
                }
        }
    }
}