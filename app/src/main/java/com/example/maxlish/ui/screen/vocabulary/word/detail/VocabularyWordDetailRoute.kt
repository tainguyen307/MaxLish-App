package com.example.maxlish.ui.screen.vocabulary.word.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun VocabularyWordDetailRoute(

    setId: String,
    wordId: String,

    onBack: () -> Unit,
    onNavigateToEdit: (String, String) -> Unit,

    viewModel: VocabularyWordDetailViewModel
) {

    val state by viewModel.state.collectAsState()

    VocabularyWordDetailScreen(

        state = state,

        onEvent = { event ->

            when (event) {

                VocabularyWordDetailEvent.OnBackClick -> {
                    onBack()
                }

                VocabularyWordDetailEvent.OnEditClick -> {
                    onNavigateToEdit(setId, wordId)
                }

                VocabularyWordDetailEvent.OnDeleteClick -> {
                    onBack()
                }

                else -> {
                    viewModel.onEvent(event)
                }
            }
        }
    )
}