package com.example.maxlish.ui.screen.vocabulary.set.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VocabularySetListRoute(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onNavigateToLearn: (String) -> Unit,
    viewModel: VocabularySetListViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    VocabularySetListScreen(
        state = state,
        onEvent = { event ->

            when (event) {

                is VocabularySetListEvent.OnSetClick -> {
                    onNavigateToDetail(event.setId)
                }

                is VocabularySetListEvent.OnCreateSetClick -> {
                    onNavigateToCreate()
                }

                is VocabularySetListEvent.OnEditClick -> {
                    onNavigateToEdit(event.setId)
                }

                is VocabularySetListEvent.OnLearnClick -> {
                    onNavigateToLearn(event.setId)
                }

                else -> {
                    viewModel.onEvent(event)
                }
            }
        }
    )
}