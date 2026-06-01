package com.example.maxlish.ui.screen.vocabulary.set.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun VocabularySetCreateRoute(
    setId: String? = null,
    viewModel: VocabularySetCreateViewModel,
    onSuccess: () -> Unit
) {

    val state = viewModel.state.collectAsState().value

    LaunchedEffect(setId) {

        if (!setId.isNullOrBlank()) {
            viewModel.loadSet(setId)
        }
    }

    if (state.success) {
        onSuccess()
    }

    VocabularySetCreateScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}