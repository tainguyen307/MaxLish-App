package com.example.maxlish.ui.screen.vocabulary.set.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VocabularySetDetailViewModel(
    private val repository: VocabularyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        VocabularySetDetailState(
            isLoading = true,
            vocabularySet = null,
            errorMessage = null
        )
    )

    val state: StateFlow<VocabularySetDetailState> =
        _state.asStateFlow()

    fun load(setId: String) {

        _state.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {

            val result = repository.getVocabularySetById(setId)

            result
                .onSuccess { data ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            vocabularySet = data,
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

    fun onEvent(event: VocabularySetDetailEvent) {
        when (event) {

            VocabularySetDetailEvent.OnBackClick -> {
                // handled in Route
            }

            VocabularySetDetailEvent.OnLearnClick -> {
                // later implement learning flow
            }

            else -> Unit
        }
    }
}