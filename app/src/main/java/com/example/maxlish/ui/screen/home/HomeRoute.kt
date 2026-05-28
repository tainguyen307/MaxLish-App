package com.example.maxlish.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeRoute(
    onNavigateToProfile: () -> Unit,
    onNavigateToProgress: () -> Unit
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
                else -> {
                    viewModel.onEvent(event)
                }
            }
        }
    )
}