package com.example.maxlish.ui.screen.vocabulary.set.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.repository.FirebaseVocabularyRepository
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel
import com.example.maxlish.data.seed.SeedData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VocabularySetListViewModel : ViewModel() {

    private val repository =
        FirebaseVocabularyRepository(
            FirebaseFirestore.getInstance()
        )

    private val learningRepository = com.example.maxlish.data.repository.FirebaseLearningRepository(
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

    private var loadJob: Job? = null

    private fun loadVocabularySets() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val auth = FirebaseAuth.getInstance()
                val userId = auth.currentUser?.uid ?: return@launch

                repository.observeVocabularySets(userId)
                    .combine(learningRepository.observeLearningProgress(userId)) { sets, progressList ->
                        if (sets.isEmpty()) {
                            viewModelScope.launch {
                                try {
                                    val email = auth.currentUser?.email ?: "newuser@example.com"
                                    Log.d("VocabularySetList", "Chưa có dữ liệu. Tiến hành tự động nạp dữ liệu mẫu...")
                                    SeedData().seedForUser(userId, email)
                                } catch (e: Exception) {
                                    Log.e("VocabularySetList", "Lỗi nạp dữ liệu tự động: ${e.message}", e)
                                }
                            }
                        }
                        sets to progressList
                    }
                    .catch { e ->
                        Log.e("VocabularySetList", "Lỗi tải dữ liệu bộ từ vựng: ${e.message}", e)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }
                    .collect { (sets, progressList) ->
                        allSets = sets.map { set ->
                            val setProgress = progressList.filter { it.setId == set.setId }
                            val learnedWords = setProgress.size
                            val progress = if (set.wordCount == 0) 0f else learnedWords.toFloat() / set.wordCount.toFloat()

                            VocabularySetUiModel(
                                id = set.setId,
                                title = set.title,
                                totalWords = set.wordCount,
                                learnedWords = learnedWords,
                                progress = progress.coerceAtMost(1f)
                            )
                        }

                        val query = _state.value.searchQuery
                        val filtered = if (query.isBlank()) {
                            allSets
                        } else {
                            allSets.filter {
                                it.title.contains(query, ignoreCase = true)
                            }
                        }

                        _state.value = _state.value.copy(
                            isLoading = false,
                            vocabularySets = filtered,
                            errorMessage = null
                        )
                    }

            } catch (e: Exception) {
                Log.e("VocabularySetList", "Lỗi launch block: ${e.message}", e)
                _state.value = _state.value.copy(
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

            VocabularySetListEvent.OnRetry -> {
                loadVocabularySets()
            }

            else -> Unit
        }
    }
}