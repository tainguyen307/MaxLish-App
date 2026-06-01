package com.example.maxlish.ui.screen.vocabulary.set.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel

private val Primary = Color(0xFF2563EB)
private val Background = Color(0xFFF8FAFC)
private val CardColor = Color.White

private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF64748B)

@Composable
fun VocabularySetListScreen(
    state: VocabularySetListState,
    onEvent: (VocabularySetListEvent) -> Unit
) {
    var deleteSetId by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(

        containerColor = Background,

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    onEvent(
                        VocabularySetListEvent
                            .OnCreateSetClick
                    )
                },
                containerColor = Primary
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {

                Column {

                    Text(
                        text = "Vocabulary Sets",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${state.vocabularySets.size} sets",
                        color = TextSecondary
                    )
                }
            }

            item {

                OutlinedTextField(

                    value = state.searchQuery,

                    onValueChange = {

                        onEvent(
                            VocabularySetListEvent
                                .OnSearchChange(it)
                        )
                    },

                    modifier = Modifier.fillMaxWidth(),

                    placeholder = {
                        Text("Search vocabulary sets...")
                    },

                    leadingIcon = {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },

                    shape = RoundedCornerShape(18.dp),

                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            items(state.vocabularySets) { set ->

                VocabularySetCard(
                    set = set,

                    onClick = {
                        onEvent(
                            VocabularySetListEvent.OnSetClick(set.id)
                        )
                    },

                    onLearnClick = {
                        onEvent(
                            VocabularySetListEvent.OnLearnClick(set.id)
                        )
                    },

                    onEditClick = {
                        onEvent(
                            VocabularySetListEvent.OnEditClick(set.id)
                        )
                    },

                    onDeleteClick = {
                        deleteSetId = set.id
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        deleteSetId?.let { setId ->

            AlertDialog(

                onDismissRequest = {
                    deleteSetId = null
                },

                title = {
                    Text("Delete Set")
                },

                text = {
                    Text(
                        "This action cannot be undone."
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            onEvent(
                                VocabularySetListEvent
                                    .OnDeleteClick(setId)
                            )

                            deleteSetId = null
                        }
                    ) {
                        Text("Delete")
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            deleteSetId = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun VocabularySetCard(
    set: VocabularySetUiModel,
    onClick: () -> Unit,
    onLearnClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // HEADER ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Primary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    // LEARN BUTTON (IMPORTANT CTA)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Primary)
                            .clickable { onLearnClick() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            "Learn",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // MORE MENU
                    Box {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable { showMenu = true }
                                .size(20.dp),
                            tint = TextSecondary
                        )

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    showMenu = false
                                    onEditClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    showMenu = false
                                    onDeleteClick()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = set.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${set.totalWords} words",
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { set.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(100.dp)),
                color = Primary,
                trackColor = Primary.copy(alpha = 0.12f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${(set.progress * 100).toInt()}% completed",
                color = Primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}