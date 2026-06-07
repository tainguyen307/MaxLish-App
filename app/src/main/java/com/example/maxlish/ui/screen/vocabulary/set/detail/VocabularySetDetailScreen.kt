package com.example.maxlish.ui.screen.vocabulary.set.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.maxlish.ui.component.DuoButton
import com.example.maxlish.ui.component.DuoCard
import com.example.maxlish.ui.component.DuoColors

@Composable
fun VocabularySetDetailScreen(
    state: VocabularySetDetailState,
    onEvent: (VocabularySetDetailEvent) -> Unit
) {
    val set = state.vocabularySet

    // Phân bổ màu sắc sinh động dựa trên tag hoặc tên bộ từ vựng
    val (primaryColor, darkColor) = when {
        set?.title?.contains("IELTS", ignoreCase = true) == true -> 
            Pair(DuoColors.Purple, DuoColors.PurpleDark)
        set?.title?.contains("TOEIC", ignoreCase = true) == true -> 
            Pair(DuoColors.Green, DuoColors.GreenDark)
        else -> 
            Pair(DuoColors.Blue, DuoColors.BlueDark)
    }

    Scaffold(
        containerColor = DuoColors.Background,
        floatingActionButton = {
            // Nút bấm thêm từ vựng 3D tròn
            DuoCard(
                modifier = Modifier.size(56.dp),
                backgroundColor = DuoColors.Blue,
                borderColor = DuoColors.BlueDark,
                shadowHeight = 4.dp,
                shape = CircleShape,
                onClick = {
                    onEvent(VocabularySetDetailEvent.OnAddWordClick)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info Card 3D
            DuoCard(
                backgroundColor = DuoColors.White,
                borderColor = DuoColors.Border,
                shadowHeight = 4.dp,
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onEvent(VocabularySetDetailEvent.OnBackClick) },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Quay lại",
                                tint = DuoColors.TextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(primaryColor.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = primaryColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = set?.title ?: "Đang tải...",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = set?.description ?: "Chưa có mô tả cho bộ từ vựng này.",
                        fontSize = 15.sp,
                        color = DuoColors.TextSecondary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(primaryColor.copy(alpha = 0.15f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${set?.wordCount ?: 0} từ vựng",
                            color = primaryColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CTA Learn 3D Button
            DuoButton(
                onClick = {
                    onEvent(VocabularySetDetailEvent.OnLearnClick)
                },
                backgroundColor = DuoColors.Green,
                bottomColor = DuoColors.GreenDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Bắt đầu học ngay",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }

            // View Words 3D Button
            DuoButton(
                onClick = {
                    onEvent(VocabularySetDetailEvent.OnViewWordsClick)
                },
                backgroundColor = DuoColors.Blue,
                bottomColor = DuoColors.BlueDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Xem danh sách từ vựng",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CSV ACTIONS Row (3D White Buttons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DuoButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEvent(VocabularySetDetailEvent.OnImportCsvClick)
                    },
                    backgroundColor = DuoColors.White,
                    bottomColor = DuoColors.WhiteDark
                ) {
                    Text(
                        text = "Nhập CSV",
                        color = DuoColors.TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }

                DuoButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEvent(VocabularySetDetailEvent.OnExportCsvClick)
                    },
                    backgroundColor = DuoColors.White,
                    bottomColor = DuoColors.WhiteDark
                ) {
                    Text(
                        text = "Xuất CSV",
                        color = DuoColors.TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }
            }

            // Error/Success status message card
            state.errorMessage?.let { msg ->
                val isSuccess = msg.contains("thành công")
                Spacer(modifier = Modifier.height(8.dp))
                DuoCard(
                    backgroundColor = if (isSuccess) DuoColors.Green.copy(alpha = 0.1f) else DuoColors.Red.copy(alpha = 0.1f),
                    borderColor = if (isSuccess) DuoColors.Green.copy(alpha = 0.3f) else DuoColors.Red.copy(alpha = 0.3f),
                    shadowHeight = 3.dp,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.padding(14.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSuccess) DuoColors.GreenDark else DuoColors.RedDark
                    )
                }
            }
        }
    }
}