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

    // Fix: SharedFlow phải dùng LaunchedEffect + collect, KHÔNG dùng collectAsState()
    // collectAsState() không hoạt động đúng với SharedFlow vì SharedFlow không giữ state
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is VocabularyWordCreateViewModel.Effect.Saved -> {
                    onSuccess()
                }
                is VocabularyWordCreateViewModel.Effect.Error -> {
                    // optional: show snackbar
                }
            }
        }
    }

    VocabularyWordCreateScreen(

        state = state,

        onEvent = { event ->

            when (event) {

                VocabularyWordCreateEvent.OnSaveClick -> {
                    viewModel.onEvent(event)
                }

                VocabularyWordCreateEvent.OnBackClick -> {
                    onBack()
                }

                else -> {
                    viewModel.onEvent(event)
                }
            }
        }
    )
}