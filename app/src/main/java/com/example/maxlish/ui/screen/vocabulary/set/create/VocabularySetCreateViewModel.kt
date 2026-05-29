package com.example.maxlish.ui.screen.vocabulary.set.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.VocabularySet
import com.example.maxlish.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VocabularySetCreateViewModel(
    private val repository: VocabularyRepository,
    private val ownerId: String
) : ViewModel() {

    private val _state = MutableStateFlow(VocabularySetCreateState())
    val state: StateFlow<VocabularySetCreateState> = _state.asStateFlow()

    fun onEvent(event: VocabularySetCreateEvent) {
        when (event) {

            is VocabularySetCreateEvent.OnTitleChange -> {
                _state.update { it.copy(title = event.value) }
            }

            is VocabularySetCreateEvent.OnDescriptionChange -> {
                _state.update { it.copy(description = event.value) }
            }

            is VocabularySetCreateEvent.OnTagInputChange -> {
                _state.update { it.copy(tagInput = event.value) }
            }

            VocabularySetCreateEvent.OnAddTag -> {
                _state.update { state ->

                    val tag = state.tagInput.trim()

                    if (tag.isBlank()) return@update state

                    val updated = state.selectedTags.toMutableList()

                    if (!updated.contains(tag)) {
                        updated.add(tag)
                    }

                    state.copy(
                        selectedTags = updated,
                        tagInput = ""
                    )
                }
            }

            is VocabularySetCreateEvent.OnRemoveTag -> {
                _state.update { state ->
                    state.copy(
                        selectedTags = state.selectedTags.filter {
                            it != event.tag
                        }
                    )
                }
            }

            VocabularySetCreateEvent.OnCreateClick -> {
                createSet()
            }
        }
    }

    private fun createSet() {

        val current = _state.value

        if (current.title.isBlank()) {
            _state.update {
                it.copy(errorMessage = "Title cannot be empty")
            }
            return
        }

        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val set = VocabularySet(
                setId = "",
                ownerId = ownerId,
                title = current.title,
                description = current.description,
                tags = current.selectedTags,
                wordCount = 0
            )

            val result = repository.createVocabularySet(set)

            result.onSuccess {
                _state.update {
                    it.copy(
                        isLoading = false,
                        success = true
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            }
        }
    }
}