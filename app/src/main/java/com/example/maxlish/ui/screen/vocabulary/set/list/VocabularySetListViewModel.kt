package com.example.maxlish.ui.screen.vocabulary.set.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.repository.FirebaseVocabularyRepository
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VocabularySetListViewModel : ViewModel() {

    private val repository =
        FirebaseVocabularyRepository(
            FirebaseFirestore.getInstance()
        )

    private val _state =
        MutableStateFlow(
            VocabularySetListState()
        )

    val state: StateFlow<VocabularySetListState> =
        _state.asStateFlow()

    private var allSets: List<VocabularySetUiModel> =
        emptyList()

    init {
        loadVocabularySets()
    }

    private fun deleteSet(setId: String) {

        viewModelScope.launch {

            repository
                .deleteVocabularySet(setId)

            loadVocabularySets()
        }
    }

    private fun loadVocabularySets() {

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true
            )

            try {

                val userId =
                    FirebaseAuth.getInstance()
                        .currentUser
                        ?.uid
                        ?: return@launch

                val result =
                    repository.getVocabularySets(userId)

                val sets =
                    result.getOrNull()
                        ?: emptyList()

                allSets =
                    sets.map {

                        VocabularySetUiModel(
                            id = it.setId,
                            title = it.title,
                            totalWords = it.wordCount,
                            progress = 0F
                        )
                    }

                _state.value =
                    _state.value.copy(
                        isLoading = false,
                        vocabularySets = allSets
                    )

            } catch (e: Exception) {

                _state.value =
                    _state.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
            }
        }
    }

    fun onEvent(
        event: VocabularySetListEvent
    ) {

        when (event) {

            is VocabularySetListEvent.OnSearchChange -> {

                val filtered =
                    allSets.filter {

                        it.title.contains(
                            event.query,
                            ignoreCase = true
                        )
                    }

                _state.value =
                    _state.value.copy(
                        searchQuery = event.query,
                        vocabularySets = filtered
                    )
            }

            is VocabularySetListEvent.OnDeleteClick -> {
                deleteSet(event.setId)
            }

            else -> Unit
        }
    }
}