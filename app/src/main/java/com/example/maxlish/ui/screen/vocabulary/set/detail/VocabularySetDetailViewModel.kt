package com.example.maxlish.ui.screen.vocabulary.set.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.repository.VocabularyRepository
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel
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

                    // MAP DATA MODEL -> UI MODEL (FIX ERROR HERE)
                    val uiModel = VocabularySetUiModel(
                        id = data.setId,
                        title = data.title,
                        totalWords = data.wordCount,
                        progress = 0f // TODO: calculate later
                    )

                    _state.update {
                        it.copy(
                            isLoading = false,
                            vocabularySet = uiModel,
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

            VocabularySetDetailEvent.OnStartLearningClick -> {
                // later implement learning flow
            }
        }
    }
}