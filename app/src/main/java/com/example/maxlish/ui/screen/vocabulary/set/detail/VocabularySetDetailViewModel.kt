package com.example.maxlish.ui.screen.vocabulary.set.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.repository.VocabularyRepository
import com.example.maxlish.data.helper.CsvHelper
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
                // handled in Route
            }

            VocabularySetDetailEvent.OnImportCsvClick -> {
                // Handled in Route using launcher
            }

            VocabularySetDetailEvent.OnExportCsvClick -> {
                // Triggered in Route using callback
            }

            else -> Unit
        }
    }

    fun importFromCsv(csvText: String) {
        val currentSet = _state.value.vocabularySet ?: return
        
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            try {
                val words = CsvHelper.parseCsv(
                    csvText = csvText,
                    ownerId = currentSet.ownerId,
                    setId = currentSet.setId
                )
                
                if (words.isEmpty()) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "File CSV trống hoặc không đúng định dạng (yêu cầu cột Word và Meaning)"
                        )
                    }
                    return@launch
                }
                
                var successCount = 0
                words.forEach { word ->
                    val res = repository.createWord(currentSet.setId, word)
                    if (res.isSuccess) {
                        successCount++
                    }
                }
                
                // Reload set to update word count
                load(currentSet.setId)
                
                _state.update {
                    it.copy(
                        errorMessage = "Đã import thành công $successCount/${words.size} từ vựng"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Lỗi khi import CSV: ${e.message}"
                    )
                }
            }
        }
    }

    fun exportToCsv(onResult: (String?) -> Unit) {
        val currentSet = _state.value.vocabularySet ?: return
        
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            try {
                val result = repository.getWordsBySetId(currentSet.setId)
                result.onSuccess { words ->
                    _state.update { it.copy(isLoading = false) }
                    if (words.isEmpty()) {
                        _state.update { it.copy(errorMessage = "Bộ từ vựng này trống, không có gì để xuất") }
                        onResult(null)
                    } else {
                        val csvString = CsvHelper.exportToCsv(words)
                        onResult(csvString)
                    }
                }.onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Lỗi khi lấy từ vựng: ${error.message}"
                        )
                    }
                    onResult(null)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Lỗi khi xuất: ${e.message}"
                    )
                }
                onResult(null)
            }
        }
    }
}