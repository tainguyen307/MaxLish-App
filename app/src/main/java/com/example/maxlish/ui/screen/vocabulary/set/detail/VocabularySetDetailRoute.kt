package com.example.maxlish.ui.screen.vocabulary.set.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun VocabularySetDetailRoute(
    setId: String,
    onBack: () -> Unit,
    onNavigateToWordList: (String) -> Unit,
    onNavigateToAddWord: (String) -> Unit,
    onNavigateToLearn: (String) -> Unit,
    viewModel: VocabularySetDetailViewModel
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(setId) {
        viewModel.load(setId)
    }

    VocabularySetDetailScreen(
        state = state,
        onEvent = { event ->
            when (event) {

                VocabularySetDetailEvent.OnBackClick -> {
                    onBack()
                }

                VocabularySetDetailEvent.OnLearnClick -> {

                    state.vocabularySet?.let {
                        onNavigateToLearn(it.setId)
                    }
                }

                VocabularySetDetailEvent.OnViewWordsClick -> {

                    state.vocabularySet?.let {
                        onNavigateToWordList(it.setId)
                    }
                }

                VocabularySetDetailEvent.OnAddWordClick -> {

                    state.vocabularySet?.let {
                        onNavigateToAddWord(it.setId)
                    }
                }
            }
        }
    )
}