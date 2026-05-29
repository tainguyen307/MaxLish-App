package com.example.maxlish.ui.screen.vocabulary.set.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun VocabularySetCreateRoute(
    viewModel: VocabularySetCreateViewModel,
    onSuccess: () -> Unit
) {

    val state = viewModel.state.collectAsState().value

    if (state.success) {
        onSuccess()
    }

    VocabularySetCreateScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}