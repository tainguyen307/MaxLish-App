package com.example.maxlish.ui.screen.vocabulary.word.list

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.data.model.VocabularyWord
import com.example.maxlish.ui.component.DuoCard
import com.example.maxlish.ui.component.DuoColors

@Composable
fun VocabularyWordListScreen(
    state: VocabularyWordListState,
    onEvent: (VocabularyWordListEvent) -> Unit
) {
    Scaffold(
        containerColor = DuoColors.Background,
        floatingActionButton = {
            // Nút bấm thêm từ 3D tròn
            DuoCard(
                modifier = Modifier.size(56.dp),
                backgroundColor = DuoColors.Blue,
                borderColor = DuoColors.BlueDark,
                shadowHeight = 4.dp,
                shape = CircleShape,
                onClick = {
                    onEvent(VocabularyWordListEvent.OnAddWordClick)
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Thêm từ mới",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DuoColors.Blue)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onEvent(VocabularyWordListEvent.OnBackClick) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = DuoColors.TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Danh Sách Từ Vựng",
                            color = DuoColors.TextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Bộ từ này có ${state.words.size} từ vựng",
                            color = DuoColors.TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = {
                        onEvent(VocabularyWordListEvent.OnSearchChange(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = DuoColors.TextSecondary
                        )
                    },
                    placeholder = {
                        Text("Tìm kiếm từ vựng...", fontWeight = FontWeight.Bold)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DuoColors.White,
                        unfocusedContainerColor = DuoColors.White,
                        focusedBorderColor = DuoColors.Blue,
                        unfocusedBorderColor = DuoColors.Border
                    )
                )
            }

            items(state.words) { word ->
                VocabularyWordCard3D(
                    word = word,
                    onClick = {
                        onEvent(VocabularyWordListEvent.OnWordClick(word.wordId))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun VocabularyWordCard3D(
    word: VocabularyWord,
    onClick: () -> Unit
) {
    DuoCard(
        backgroundColor = DuoColors.White,
        borderColor = DuoColors.Border,
        shadowHeight = 4.dp,
        shape = RoundedCornerShape(22.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = word.word,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = word.pronunciation,
                        fontSize = 13.sp,
                        color = DuoColors.Orange,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = word.meaning,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuoColors.TextSecondary
                )
            }

            // Độ khó dưới dạng nhãn màu 3D nhạt
            val (labelColor, labelBg) = when (word.difficulty) {
                "Easy" -> Pair(DuoColors.GreenDark, DuoColors.Green.copy(alpha = 0.15f))
                "Hard" -> Pair(DuoColors.RedDark, DuoColors.Red.copy(alpha = 0.15f))
                else -> Pair(DuoColors.OrangeDark, DuoColors.Orange.copy(alpha = 0.15f))
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(labelBg)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = when (word.difficulty) {
                        "Easy" -> "DỄ"
                        "Hard" -> "KHÓ"
                        else -> "TRUNG BÌNH"
                    },
                    color = labelColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp
                )
            }
        }
    }
}