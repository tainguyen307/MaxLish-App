package com.example.maxlish.ui.screen.vocabulary.set.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun VocabularySetCreateScreen(
    state: VocabularySetCreateState,
    onEvent: (VocabularySetCreateEvent) -> Unit
) {

    var localTitle by remember { mutableStateOf(TextFieldValue(state.title)) }
    var localDesc by remember { mutableStateOf(TextFieldValue(state.description)) }

    LaunchedEffect(state.title) {
        if (state.title != localTitle.text) {
            localTitle = TextFieldValue(state.title)
        }
    }

    LaunchedEffect(state.description) {
        if (state.description != localDesc.text) {
            localDesc = TextFieldValue(state.description)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text("DECK NAME", style = MaterialTheme.typography.labelSmall)

                OutlinedTextField(
                    value = localTitle,
                    onValueChange = {
                        localTitle = it
                        onEvent(VocabularySetCreateEvent.OnTitleChange(it.text))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Travel Essentials") }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text("DESCRIPTION", style = MaterialTheme.typography.labelSmall)

                OutlinedTextField(
                    value = localDesc,
                    onValueChange = {
                        localDesc = it
                        onEvent(VocabularySetCreateEvent.OnDescriptionChange(it.text))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = { Text("Explain what this deck covers...") }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("TAGS", style = MaterialTheme.typography.labelSmall)

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            state.selectedTags.forEach { tag ->

                AssistChip(
                    onClick = {
                        onEvent(
                            VocabularySetCreateEvent.OnRemoveTag(tag)
                        )
                    },
                    label = { Text(tag) },
                    trailingIcon = {
                        Text("×")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.tagInput,
            onValueChange = {
                onEvent(
                    VocabularySetCreateEvent.OnTagInputChange(it)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Add tag...") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onEvent(VocabularySetCreateEvent.OnAddTag)
            }
        ) {
            Text("Add Tag")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onEvent(VocabularySetCreateEvent.OnCreateClick)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(8.dp))
            CircularProgressIndicator()
        }
    }
}