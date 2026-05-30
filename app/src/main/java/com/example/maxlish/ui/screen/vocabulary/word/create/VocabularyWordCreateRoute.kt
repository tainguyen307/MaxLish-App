package com.example.maxlish.ui.screen.vocabulary.word.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun VocabularyWordCreateRoute(

    setId: String,
    wordId: String? = null,

    onSuccess: () -> Unit,
    onBack: () -> Unit,

    viewModel: VocabularyWordCreateViewModel
) {

    val state by viewModel.state.collectAsState()
    val effect = viewModel.effect.collectAsState(initial = null)

    LaunchedEffect(effect.value) {
        when (effect.value) {

            is VocabularyWordCreateViewModel.Effect.Saved -> {
                onBack()
            }

            is VocabularyWordCreateViewModel.Effect.Error -> {
                // optional: show toast/snackbar
            }

            null -> {}
        }
    }

    VocabularyWordCreateScreen(

        state = state,

        onEvent = { event ->

            when (event) {

                VocabularyWordCreateEvent.OnSaveClick -> {
                    viewModel.onEvent(event)
                }

                else -> {
                    viewModel.onEvent(event)
                }
            }
        }
    )
}