package com.example.maxlish.ui.screen.vocabulary.word.create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VocabularyWordCreateScreen(

    state: VocabularyWordCreateState,
    onEvent: (VocabularyWordCreateEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text =
                if (state.isEditMode)
                    "Edit Word"
                else
                    "Create Word",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.word,
            onValueChange = {
                onEvent(
                    VocabularyWordCreateEvent
                        .OnWordChange(it)
                )
            },
            label = { Text("Word") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.meaning,
            onValueChange = {
                onEvent(
                    VocabularyWordCreateEvent
                        .OnMeaningChange(it)
                )
            },
            label = { Text("Meaning") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.pronunciation,
            onValueChange = {
                onEvent(
                    VocabularyWordCreateEvent
                        .OnPronunciationChange(it)
                )
            },
            label = { Text("Pronunciation") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.description,
            onValueChange = {
                onEvent(
                    VocabularyWordCreateEvent
                        .OnDescriptionChange(it)
                )
            },
            label = { Text("Description") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.example,
            onValueChange = {
                onEvent(
                    VocabularyWordCreateEvent
                        .OnExampleChange(it)
                )
            },
            label = { Text("Example") }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                onEvent(
                    VocabularyWordCreateEvent.OnSaveClick
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}