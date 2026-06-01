package com.example.maxlish.ui.screen.vocabulary.set.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VocabularySetDetailScreen(
    state: VocabularySetDetailState,
    onEvent: (VocabularySetDetailEvent) -> Unit
) {

    val set = state.vocabularySet

    Scaffold(

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    onEvent(
                        VocabularySetDetailEvent.OnAddWordClick
                    )
                }
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = set?.title ?: "Loading...",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = set?.description ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "${set?.wordCount ?: 0} words",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onEvent(
                        VocabularySetDetailEvent.OnLearnClick
                    )
                }
            ) {

                Text("Learn")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onEvent(
                        VocabularySetDetailEvent.OnViewWordsClick
                    )
                }
            ) {

                Text("View Words")
            }

            // CSV ACTIONS Row
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEvent(VocabularySetDetailEvent.OnImportCsvClick)
                    }
                ) {
                    Text("Import CSV")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEvent(VocabularySetDetailEvent.OnExportCsvClick)
                    }
                ) {
                    Text("Export CSV")
                }
            }

            // Error/Success status message card
            state.errorMessage?.let { msg ->
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (msg.contains("thành công")) MaterialTheme.colorScheme.primaryContainer 
                                         else MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (msg.contains("thành công")) MaterialTheme.colorScheme.onPrimaryContainer 
                                else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}