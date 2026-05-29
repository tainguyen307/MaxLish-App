package com.example.maxlish.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun HomeRoute(
    onNavigateToProfile: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToVocabularySetDetail: (String) -> Unit
) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    HomeScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                HomeEvent.OnProfileClick -> {
                    onNavigateToProfile()
                }
                HomeEvent.OnProgressClick -> {
                    onNavigateToProgress()
                }
                is HomeEvent.OnVocabularySetClick -> {
                    onNavigateToVocabularySetDetail(event.setId)
                }
                else -> {
                    viewModel.onEvent(event)
                }
            }
        }
    )
}