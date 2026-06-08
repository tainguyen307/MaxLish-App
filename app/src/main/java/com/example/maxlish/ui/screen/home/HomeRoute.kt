package com.example.maxlish.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeRoute(
    onNavigateToProfile: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToVocabularySetDetail: (String) -> Unit,
    onNavigateToLearn: (String, String) -> Unit
) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
    }

    HomeScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                HomeEvent.OnProfileClick -> onNavigateToProfile()
                HomeEvent.OnProgressClick -> onNavigateToProgress()
                is HomeEvent.OnVocabularySetClick ->
                    onNavigateToVocabularySetDetail(event.setId)
                HomeEvent.OnContinueLearningClick -> {
                    state.currentVocabularySetId?.let { id ->
                        onNavigateToLearn(id, "all")
                    }
                }
                HomeEvent.OnReviewClick -> {
                    state.currentVocabularySetId?.let { id ->
                        onNavigateToLearn(id, "review")
                    }
                }
                else -> viewModel.onEvent(event)
            }
        }
    )
}