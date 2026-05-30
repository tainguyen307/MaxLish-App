package com.example.maxlish.ui.screen.vocabulary.word.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.VocabularyWord
import com.example.maxlish.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VocabularyWordListViewModel(
    private val repository: VocabularyRepository,
    private val setId: String
) : ViewModel() {

    private val _state =
        MutableStateFlow(
            VocabularyWordListState(
                isLoading = true
            )
        )

    val state: StateFlow<VocabularyWordListState> =
        _state.asStateFlow()

    private var allWords =
        emptyList<VocabularyWord>()

    init {
        loadWords()
    }

    fun loadWords() {

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            repository
                .getWordsBySetId(setId)
                .onSuccess { words ->

                    allWords = words

                    _state.update {
                        it.copy(
                            isLoading = false,
                            words = words,
                            errorMessage = null
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

    fun onEvent(
        event: VocabularyWordListEvent
    ) {

        when (event) {

            is VocabularyWordListEvent.OnSearchChange -> {

                val filtered =
                    allWords.filter {

                        it.word.contains(
                            event.query,
                            ignoreCase = true
                        ) ||

                                it.meaning.contains(
                                    event.query,
                                    ignoreCase = true
                                )
                    }

                _state.update {
                    it.copy(
                        searchQuery = event.query,
                        words = filtered
                    )
                }
            }

            else -> Unit
        }
    }

    fun refresh() {
        loadWords()
    }
}