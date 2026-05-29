package com.example.maxlish.ui.screen.vocabulary.set.detail

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun VocabularySetDetailRoute(
    setId: String,
    onBack: () -> Unit,
    onStartLearning: (String) -> Unit,
    viewModel: VocabularySetDetailViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    VocabularySetDetailScreen(
        state = state,
        onEvent = { event ->
            when (event) {

                VocabularySetDetailEvent.OnBackClick -> {
                    onBack()
                }

                VocabularySetDetailEvent.OnStartLearningClick -> {
                    onStartLearning(setId)
                }

                else -> {
                    viewModel.onEvent(event)
                }
            }
        }
    )
}