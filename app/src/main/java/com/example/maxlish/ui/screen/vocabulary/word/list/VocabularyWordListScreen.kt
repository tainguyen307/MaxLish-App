package com.example.maxlish.ui.screen.vocabulary.word.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.maxlish.data.model.VocabularyWord

@Composable
fun VocabularyWordListScreen(

    state: VocabularyWordListState,

    onEvent: (VocabularyWordListEvent) -> Unit
) {

    Scaffold(

        floatingActionButton = {

            FloatingActionButton(

                onClick = {

                    onEvent(
                        VocabularyWordListEvent
                            .OnAddWordClick
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

        if (state.isLoading) {

            CircularProgressIndicator()

            return@Scaffold
        }

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            contentPadding = PaddingValues(
                16.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            item {

                OutlinedTextField(

                    value = state.searchQuery,

                    onValueChange = {

                        onEvent(
                            VocabularyWordListEvent
                                .OnSearchChange(it)
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Search,
                            contentDescription = null
                        )
                    },

                    placeholder = {
                        Text("Search words...")
                    }
                )
            }

            items(state.words) { word ->

                VocabularyWordCard(

                    word = word,

                    onClick = {

                        onEvent(
                            VocabularyWordListEvent
                                .OnWordClick(
                                    word.wordId
                                )
                        )
                    }
                )
            }

            item {

                Spacer(
                    modifier =
                        Modifier.height(80.dp)
                )
            }
        }
    }
}

@Composable
private fun VocabularyWordCard(
    word: VocabularyWord,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = word.word)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = word.meaning)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = word.difficulty)
        }
    }
}