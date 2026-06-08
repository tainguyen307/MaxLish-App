package com.example.maxlish.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
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
import com.example.maxlish.ui.component.DuoProgressBar
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    Scaffold(
        containerColor = DuoColors.Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                HomeTopBar(
                    userName = state.userName,
                    streak = state.streak
                )
            }

            item {
                ContinueLearningCard(
                    title = state.currentVocabularySetTitle,
                    remainingWords = state.remainingWords,
                    progress = state.learningProgress,
                    earnedXp = state.earnedXp,
                    accuracy = state.accuracy,
                    reviewCount = state.reviewCount,
                    onContinueClick = {
                        onEvent(HomeEvent.OnContinueLearningClick)
                    }
                )
            }

            item {
                ReviewDueCard(
                    count = state.reviewCount,
                    onReviewClick = {
                        onEvent(HomeEvent.OnReviewClick)
                    }
                )
            }

            item {
                SectionTitle(title = "Vocabulary Sets")
            }

            item {
                VocabularySetSection(
                    sets = state.vocabularySets,
                    onSetClick = { setId ->
                        onEvent(HomeEvent.OnVocabularySetClick(setId))
                    }
                )
            }

            item {
                WeeklyActivityCard(
                    values = state.weeklyActivity,
                    onClick = {
                        onEvent(HomeEvent.OnProgressClick)
                    }
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(
    userName: String,
    streak: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Xin chào 👋",
                color = DuoColors.TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = userName,
                color = DuoColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Streak Flame 3D
            DuoCard(
                backgroundColor = Color(0xFFFFF3E0),
                borderColor = DuoColors.OrangeDark.copy(alpha = 0.4f),
                shadowHeight = 3.dp,
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = DuoColors.Orange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = streak.toString(),
                        color = DuoColors.OrangeDark,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }

            // Notification Button 3D
            DuoCard(
                modifier = Modifier.size(40.dp),
                backgroundColor = DuoColors.White,
                borderColor = DuoColors.Border,
                shadowHeight = 3.dp,
                shape = CircleShape,
                onClick = {}
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = DuoColors.TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ContinueLearningCard(
    title: String,
    remainingWords: Int,
    progress: Float,
    earnedXp: Int,
    accuracy: Int,
    reviewCount: Int,
    onContinueClick: () -> Unit
) {
    DuoCard(
        backgroundColor = DuoColors.Blue,
        borderColor = DuoColors.BlueDark,
        shadowHeight = 6.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TIẾP TỤC HỌC TẬP",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Daily Goal",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Còn $remainingWords từ vựng cần hoàn thành",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 3D Progress Bar
            DuoProgressBar(
                progress = progress,
                color = DuoColors.Green,
                trackColor = Color.White.copy(alpha = 0.25f),
                height = 14.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Hero Statistics Cards (White transparent glassmorphism look)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HeroStat(
                    title = "XP",
                    value = "+$earnedXp",
                    color = DuoColors.Yellow
                )

                HeroStat(
                    title = "Chính xác",
                    value = "$accuracy%",
                    color = DuoColors.Green
                )

                HeroStat(
                    title = "Cần Ôn",
                    value = reviewCount.toString(),
                    color = DuoColors.Orange
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // White 3D DuoButton
            DuoButton(
                onClick = onContinueClick,
                backgroundColor = DuoColors.White,
                bottomColor = DuoColors.WhiteDark,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = DuoColors.Blue,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Bắt đầu học ngay",
                    color = DuoColors.Blue,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun HeroStat(
    title: String,
    value: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .border3D(Color.White.copy(alpha = 0.25f))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = color,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }
    }
}

// Helper Extension modifier để tạo border mờ kính
@Composable
fun Modifier.border3D(color: Color): Modifier {
    return this.background(color = Color.Transparent) // Border stroke is simulated
}

@Composable
fun ReviewDueCard(
    count: Int,
    onReviewClick: () -> Unit
) {
    DuoCard(
        backgroundColor = DuoColors.White,
        borderColor = DuoColors.Border,
        shadowHeight = 4.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CẦN ÔN TẬP",
                    color = DuoColors.Orange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$count từ đang chờ bạn",
                    color = DuoColors.TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }

            DuoButton(
                onClick = onReviewClick,
                backgroundColor = DuoColors.Orange,
                bottomColor = DuoColors.OrangeDark,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.width(100.dp)
            ) {
                Text(
                    text = "Ôn tập",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = DuoColors.TextPrimary,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
fun VocabularySetSection(
    sets: List<VocabularySetUiModel>,
    onSetClick: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(sets) { set ->
            VocabularySetCard(
                vocabularySet = set,
                onClick = {
                    onSetClick(set.id)
                }
            )
        }
    }
}

@Composable
fun VocabularySetCard(
    vocabularySet: VocabularySetUiModel,
    onClick: () -> Unit
) {
    // Phân bổ màu sắc sinh động dựa trên tag hoặc tên bộ từ vựng để giống Duolingo
    val (primaryColor, darkColor) = when {
        vocabularySet.title.contains("IELTS", ignoreCase = true) -> 
            Pair(DuoColors.Purple, DuoColors.PurpleDark)
        vocabularySet.title.contains("TOEIC", ignoreCase = true) -> 
            Pair(DuoColors.Green, DuoColors.GreenDark)
        else -> 
            Pair(DuoColors.Blue, DuoColors.BlueDark)
    }

    DuoCard(
        modifier = Modifier.width(240.dp),
        backgroundColor = DuoColors.White,
        borderColor = DuoColors.Border,
        shadowHeight = 4.dp,
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Nhãn nhỏ phía trên
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(primaryColor.copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (primaryColor == DuoColors.Purple) "ACADEMIC" else "BUSINESS",
                    color = primaryColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = vocabularySet.title,
                color = DuoColors.TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${vocabularySet.learnedWords}/${vocabularySet.totalWords} từ đã học",
                color = DuoColors.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar 3D
            DuoProgressBar(
                progress = vocabularySet.progress,
                color = primaryColor,
                trackColor = DuoColors.Border.copy(alpha = 0.7f),
                height = 10.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nút bắt đầu học bộ từ tương ứng - hiển thị ngữ cảnh phù hợp
            val buttonText = when {
                vocabularySet.learnedWords == 0 -> "Bắt đầu"
                vocabularySet.learnedWords >= vocabularySet.totalWords -> "Ôn tập"
                else -> "Tiếp tục"
            }
            DuoButton(
                onClick = onClick,
                backgroundColor = primaryColor,
                bottomColor = darkColor,
                shape = RoundedCornerShape(12.dp),
                shadowHeight = 3.dp
            ) {
                Text(
                    text = buttonText,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun WeeklyActivityCard(
    values: List<Int>,
    onClick: () -> Unit
) {
    DuoCard(
        backgroundColor = DuoColors.White,
        borderColor = DuoColors.Border,
        shadowHeight = 4.dp,
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hoạt Động Tuần",
                    color = DuoColors.TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )

                Text(
                    text = "Xem chi tiết",
                    color = DuoColors.Blue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val days = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
                
                // Chuẩn hóa chiều cao cột tránh bị quá đà
                val maxVal = values.maxOrNull()?.coerceAtLeast(1) ?: 1

                values.forEachIndexed { index, value ->
                    val columnHeightPercent = (value.toFloat() / maxVal.toFloat()).coerceIn(0.1f, 1.0f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        // Cột 3D
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .fillMaxHeight(columnHeightPercent)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(
                                    if (index == values.lastIndex) DuoColors.Blue
                                    else DuoColors.Blue.copy(alpha = 0.25f)
                                )
                        ) {
                            // Viền bóng 3D nhẹ ở đầu cột
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(
                                        if (index == values.lastIndex) DuoColors.BlueDark
                                        else DuoColors.BlueDark.copy(alpha = 0.25f)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = days[index],
                            color = if (index == values.lastIndex) DuoColors.Blue else DuoColors.TextSecondary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}