package com.example.maxlish.ui.screen.vocabulary.set.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VocabularySetDetailScreen(
    state: VocabularySetDetailState,
    onEvent: (VocabularySetDetailEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = state.vocabularySet?.title ?: "Loading...",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onEvent(VocabularySetDetailEvent.OnStartLearningClick)
            }
        ) {
            Text("Start Learning")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = {
                onEvent(VocabularySetDetailEvent.OnBackClick)
            }
        ) {
            Text("Back")
        }
    }
}