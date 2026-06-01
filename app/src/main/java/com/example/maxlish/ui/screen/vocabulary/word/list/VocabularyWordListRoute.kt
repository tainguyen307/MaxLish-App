package com.example.maxlish.ui.screen.vocabulary.word.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun VocabularyWordListRoute(

    onNavigateToDetail: (String) -> Unit,

    onNavigateToCreate: () -> Unit,

    viewModel: VocabularyWordListViewModel
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWords()
    }

    VocabularyWordListScreen(

        state = state,

        onEvent = { event ->

            when (event) {

                is VocabularyWordListEvent.OnWordClick -> {

                    onNavigateToDetail(
                        event.wordId
                    )
                }

                VocabularyWordListEvent.OnAddWordClick -> {

                    onNavigateToCreate()
                }

                else -> {

                    viewModel.onEvent(event)
                }
            }
        }
    )
}