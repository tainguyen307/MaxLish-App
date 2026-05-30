package com.example.maxlish.ui.screen.vocabulary.word.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VocabularyWordDetailScreen(

    state: VocabularyWordDetailState,

    onEvent: (VocabularyWordDetailEvent) -> Unit
) {

    val word = state.word

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
        return
    }

    if (word == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Word not found")
        }
        return
    }

    // =========================
    // DELETE CONFIRM DIALOG
    // =========================
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete word?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onEvent(VocabularyWordDetailEvent.OnDeleteClick)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = word.word,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        Text("Meaning: ${word.meaning}")
        Spacer(Modifier.height(8.dp))

        Text("Pronunciation: ${word.pronunciation}")
        Spacer(Modifier.height(8.dp))

        Text("Example: ${word.example}")
        Spacer(Modifier.height(8.dp))

        Text("Difficulty: ${word.difficulty}")

        Spacer(Modifier.height(20.dp))

        Row {

            Button(
                onClick = {
                    onEvent(VocabularyWordDetailEvent.OnEditClick)
                }
            ) {
                Text("Edit")
            }

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = {
                    showDeleteDialog = true
                }
            ) {
                Text("Delete")
            }
        }

        Spacer(Modifier.height(12.dp))

        TextButton(
            onClick = {
                onEvent(VocabularyWordDetailEvent.OnBackClick)
            }
        ) {
            Text("Back")
        }
    }
}