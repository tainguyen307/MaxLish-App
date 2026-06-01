package com.example.maxlish.ui.screen.vocabulary.word.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.ui.component.DuoButton
import com.example.maxlish.ui.component.DuoCard
import com.example.maxlish.ui.component.DuoColors

@Composable
fun VocabularyWordDetailScreen(
    state: VocabularyWordDetailState,
    onEvent: (VocabularyWordDetailEvent) -> Unit
) {
    val word = state.word
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = DuoColors.Blue)
        }
        return
    }

    if (word == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Không tìm thấy từ vựng", fontWeight = FontWeight.Bold, color = DuoColors.TextSecondary)
        }
        return
    }

    // =========================
    // DELETE CONFIRM DIALOG
    // =========================
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xóa từ vựng?", fontWeight = FontWeight.ExtraBold, color = DuoColors.TextPrimary) },
            text = { Text("Hành động này sẽ xóa từ vựng vĩnh viễn khỏi bộ từ của bạn.", fontWeight = FontWeight.Bold, color = DuoColors.TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onEvent(VocabularyWordDetailEvent.OnDeleteClick)
                    }
                ) {
                    Text("Xóa", color = DuoColors.Red, fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Hủy", color = DuoColors.TextSecondary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Scaffold(
        containerColor = DuoColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            DuoCard(
                backgroundColor = DuoColors.White,
                borderColor = DuoColors.Border,
                shadowHeight = 4.dp,
                shape = RoundedCornerShape(26.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(DuoColors.Blue.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = DuoColors.Blue,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = word.word,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = word.pronunciation,
                        fontSize = 18.sp,
                        color = DuoColors.Orange,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Độ khó Label
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(
                                when (word.difficulty) {
                                    "Easy" -> DuoColors.Green.copy(alpha = 0.15f)
                                    "Hard" -> DuoColors.Red.copy(alpha = 0.15f)
                                    else -> DuoColors.Orange.copy(alpha = 0.15f)
                                }
                            )
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = when (word.difficulty) {
                                "Easy" -> "Mức độ: DỄ"
                                "Hard" -> "Mức độ: KHÓ"
                                else -> "Mức độ: TRUNG BÌNH"
                            },
                            color = when (word.difficulty) {
                                "Easy" -> DuoColors.GreenDark
                                "Hard" -> DuoColors.RedDark
                                else -> DuoColors.OrangeDark
                            },
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Định nghĩa Block
            DuoCard(
                backgroundColor = DuoColors.Green.copy(alpha = 0.08f),
                borderColor = DuoColors.Green.copy(alpha = 0.25f),
                shadowHeight = 3.dp,
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ĐỊNH NGHĨA",
                        color = DuoColors.GreenDark,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = word.meaning,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoColors.TextPrimary
                    )
                }
            }

            // Definition (English)
            if (word.description.isNotBlank()) {
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = "ENGLISH DEFINITION",
                        color = DuoColors.TextSecondary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = word.description,
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        color = DuoColors.TextPrimary
                    )
                }
            }

            // Ví dụ thực tế Block
            if (word.example.isNotBlank()) {
                DuoCard(
                    backgroundColor = DuoColors.Blue.copy(alpha = 0.08f),
                    borderColor = DuoColors.Blue.copy(alpha = 0.25f),
                    shadowHeight = 3.dp,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "VÍ DỤ THỰC TẾ",
                            color = DuoColors.BlueDark,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"${word.example}\"",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoColors.TextPrimary
                        )
                    }
                }
            }

            // Collocations & Related Words
            if (word.collocations.isNotEmpty()) {
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = "COLLOCATIONS",
                        color = DuoColors.TextSecondary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        word.collocations.forEach { col ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DuoColors.Border.copy(alpha = 0.7f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = col,
                                    color = DuoColors.TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            if (word.relatedWords.isNotEmpty()) {
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = "TỪ LIÊN QUAN",
                        color = DuoColors.TextSecondary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        word.relatedWords.forEach { rel ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DuoColors.Purple.copy(alpha = 0.12f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = rel,
                                    color = DuoColors.Purple,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            // Note
            if (word.note.isNotBlank()) {
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = "GHI CHÚ CÁ NHÂN",
                        color = DuoColors.TextSecondary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = word.note,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = DuoColors.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons 3D Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Edit DuoButton Blue
                DuoButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEvent(VocabularyWordDetailEvent.OnEditClick)
                    },
                    backgroundColor = DuoColors.Blue,
                    bottomColor = DuoColors.BlueDark
                ) {
                    Text(
                        text = "Chỉnh sửa",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }

                // Delete DuoButton Red
                DuoButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showDeleteDialog = true
                    },
                    backgroundColor = DuoColors.Red,
                    bottomColor = DuoColors.RedDark
                ) {
                    Text(
                        text = "Xóa từ",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }
            }

            // Back DuoButton White
            DuoButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onEvent(VocabularyWordDetailEvent.OnBackClick)
                },
                backgroundColor = DuoColors.White,
                bottomColor = DuoColors.WhiteDark
            ) {
                Text(
                    text = "Quay lại",
                    color = DuoColors.TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}