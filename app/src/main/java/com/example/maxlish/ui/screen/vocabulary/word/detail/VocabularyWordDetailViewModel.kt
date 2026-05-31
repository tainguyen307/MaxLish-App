package com.example.maxlish.ui.screen.vocabulary.word.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.VocabularyWord
import com.example.maxlish.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VocabularyWordDetailViewModel(
    private val repository: VocabularyRepository,
    private val setId: String,
    private val wordId: String
) : ViewModel() {

    private val _state =
        MutableStateFlow(VocabularyWordDetailState(isLoading = true))

    val state: StateFlow<VocabularyWordDetailState> =
        _state.asStateFlow()

    init {
        loadWord()
    }

    private fun loadWord() {

        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            val result =
                repository.getWordById(setId, wordId)

            result
                .onSuccess { word ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            word = word
                        )
                    }
                }
                .onFailure { error ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    fun onEvent(event: VocabularyWordDetailEvent) {
        when (event) {

            VocabularyWordDetailEvent.OnDeleteClick -> {

                viewModelScope.launch {

                    val current = _state.value.word ?: return@launch

                    val result = repository.deleteWord(setId, current.wordId)

                    if (result.isSuccess) {
                        _state.update {
                            it.copy(isDeleted = true)
                        }
                    }
                }
            }

            else -> Unit
        }
    }
}