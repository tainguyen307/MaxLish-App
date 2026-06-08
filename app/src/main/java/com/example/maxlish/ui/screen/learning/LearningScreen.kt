package com.example.maxlish.ui.screen.learning

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.data.model.ReviewQuality
import com.example.maxlish.data.model.VocabularyWord
import com.example.maxlish.ui.component.DuoButton
import com.example.maxlish.ui.component.DuoCard
import com.example.maxlish.ui.component.DuoColors
import com.example.maxlish.ui.component.DuoProgressBar

@Composable
fun LearningRoute(
    viewModel: LearningViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LearningScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(
    state: LearningState,
    onEvent: (LearningEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = DuoColors.Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = state.setName,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = DuoColors.TextPrimary
                        )
                        Text(
                            text = when (state.mode) {
                                "new" -> "Daily Plan - Học từ mới"
                                "review" -> "Daily Plan - Ôn tập"
                                else -> "Daily Plan - Học & Ôn tập"
                            },
                            fontSize = 12.sp,
                            color = DuoColors.TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Close, 
                            contentDescription = "Close", 
                            tint = DuoColors.TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DuoColors.Background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DuoColors.Green)
                }
            } else if (state.errorMessage != null && state.queue.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = DuoColors.Blue,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = state.errorMessage,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoColors.TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    DuoButton(
                        onClick = onBack,
                        backgroundColor = DuoColors.Blue,
                        bottomColor = DuoColors.BlueDark,
                        modifier = Modifier.width(160.dp)
                    ) {
                        Text(
                            text = "Quay lại",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // 1. 3D DuoProgressBar
                    // Use initialQueueSize (not queue.size) so the denominator stays stable
                    // even as AGAIN cards are re-appended to the queue during the session.
                    val totalForProgress = if (state.initialQueueSize > 0) state.initialQueueSize else state.queue.size
                    val progressValue = if (totalForProgress > 0) {
                        state.currentIndex.toFloat() / totalForProgress.toFloat()
                    } else 0f

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tiến độ: ${state.currentIndex}/$totalForProgress",
                                color = DuoColors.TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${(progressValue * 100).toInt()}%",
                                color = DuoColors.Green,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                        }

                        DuoProgressBar(
                            progress = progressValue,
                            color = DuoColors.Green,
                            trackColor = DuoColors.Border.copy(alpha = 0.7f),
                            height = 14.dp
                        )
                    }

                    // 2. Flashcard with 3D flip animation
                    Spacer(modifier = Modifier.height(12.dp))
                    state.currentWord?.let { word ->
                        Flashcard(
                            word = word,
                            isFlipped = state.isFlipped,
                            onFlip = { onEvent(LearningEvent.FlipCard) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // 3. SM-2 Quality Buttons or Tap Hint
                    if (state.isFlipped) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Bạn nhớ từ này ở mức độ nào?",
                                color = DuoColors.TextSecondary,
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                QualityButton3D(
                                    modifier = Modifier.weight(1f),
                                    text = "Again",
                                    description = "Quên",
                                    color = DuoColors.Red,
                                    darkColor = DuoColors.RedDark,
                                    onClick = { onEvent(LearningEvent.RateCard(ReviewQuality.AGAIN)) }
                                )
                                QualityButton3D(
                                    modifier = Modifier.weight(1f),
                                    text = "Hard",
                                    description = "Khó",
                                    color = DuoColors.Orange,
                                    darkColor = DuoColors.OrangeDark,
                                    onClick = { onEvent(LearningEvent.RateCard(ReviewQuality.HARD)) }
                                )
                                QualityButton3D(
                                    modifier = Modifier.weight(1f),
                                    text = "Good",
                                    description = "Nhớ",
                                    color = DuoColors.Blue,
                                    darkColor = DuoColors.BlueDark,
                                    onClick = { onEvent(LearningEvent.RateCard(ReviewQuality.GOOD)) }
                                )
                                QualityButton3D(
                                    modifier = Modifier.weight(1f),
                                    text = "Easy",
                                    description = "Dễ",
                                    color = DuoColors.Green,
                                    darkColor = DuoColors.GreenDark,
                                    onClick = { onEvent(LearningEvent.RateCard(ReviewQuality.EASY)) }
                                )
                            }
                        }
                    } else {
                        // Hint tap card with 3D button-like look
                        DuoCard(
                            backgroundColor = DuoColors.White,
                            borderColor = DuoColors.Border,
                            shadowHeight = 3.dp,
                            shape = RoundedCornerShape(100.dp),
                            onClick = { onEvent(LearningEvent.FlipCard) },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Loop,
                                    contentDescription = null,
                                    tint = DuoColors.Blue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Chạm vào thẻ để lật xem nghĩa",
                                    color = DuoColors.Blue,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Summary Overlay when Finished
            if (state.isFinished) {
                SummaryOverlay(
                    correctCount = state.correctCount,
                    wrongCount = state.wrongCount,
                    xpEarned = state.xpEarned,
                    onComplete = onBack
                )
            }
        }
    }
}

@Composable
fun Flashcard(
    word: VocabularyWord,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "CardFlip"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 14 * density
            }
            .clickable { onFlip() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = DuoColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(2.dp, DuoColors.Border)
    ) {
        if (rotation <= 90f) {
            // FRONT CONTENT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(DuoColors.Blue.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = DuoColors.Blue,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = word.word,
                    color = DuoColors.TextPrimary,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = word.pronunciation,
                    color = DuoColors.Orange,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))
                
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
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = when (word.difficulty) {
                            "Easy" -> "DỄ"
                            "Hard" -> "KHÓ"
                            else -> "TRUNG BÌNH"
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
        } else {
            // BACK CONTENT
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationY = 180f
                    }
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header of card back
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = word.word,
                            color = DuoColors.TextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = word.pronunciation,
                            color = DuoColors.TextSecondary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Meaning Block (Green Accent)
                    DuoCard(
                        backgroundColor = DuoColors.Green.copy(alpha = 0.08f),
                        borderColor = DuoColors.Green.copy(alpha = 0.25f),
                        shadowHeight = 3.dp,
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
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
                                color = DuoColors.TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    // Description (English)
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
                                color = DuoColors.TextPrimary,
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Example Block (Blue Accent)
                    if (word.example.isNotBlank()) {
                        DuoCard(
                            backgroundColor = DuoColors.Blue.copy(alpha = 0.08f),
                            borderColor = DuoColors.Blue.copy(alpha = 0.25f),
                            shadowHeight = 3.dp,
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
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
                                    color = DuoColors.TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Collocation & Related Words
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
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
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
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
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

                    // Notes
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
                                color = DuoColors.TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QualityButton3D(
    modifier: Modifier = Modifier,
    text: String,
    description: String,
    color: Color,
    darkColor: Color,
    onClick: () -> Unit
) {
    DuoButton(
        onClick = onClick,
        backgroundColor = color,
        bottomColor = darkColor,
        shape = RoundedCornerShape(18.dp),
        shadowHeight = 4.dp,
        modifier = modifier.height(68.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SummaryOverlay(
    correctCount: Int,
    wrongCount: Int,
    xpEarned: Int,
    onComplete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        DuoCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            backgroundColor = DuoColors.White,
            borderColor = DuoColors.Border,
            shadowHeight = 6.dp,
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Trophy/Celebration Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFEF3C7),
                                    Color(0xFFFDE68A)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = null,
                        tint = DuoColors.Orange,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "TUYỆT VỜI!",
                        color = DuoColors.GreenDark,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Bạn đã hoàn thành phiên học hôm nay",
                        color = DuoColors.TextSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Stats Dashboard DuoCard Grid
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SummaryStatCard3D(
                            modifier = Modifier.weight(1f),
                            label = "XP NHẬN ĐƯỢC",
                            value = "+$xpEarned",
                            color = DuoColors.Orange
                        )

                        val accuracy = if (correctCount + wrongCount > 0) {
                            (correctCount * 100) / (correctCount + wrongCount)
                        } else 100
                        SummaryStatCard3D(
                            modifier = Modifier.weight(1f),
                            label = "ĐỘ CHÍNH XÁC",
                            value = "$accuracy%",
                            color = DuoColors.Green
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SummaryStatCard3D(
                            modifier = Modifier.weight(1f),
                            label = "TRẢ LỜI ĐÚNG",
                            value = "$correctCount",
                            color = DuoColors.Blue
                        )

                        SummaryStatCard3D(
                            modifier = Modifier.weight(1f),
                            label = "CẦN ÔN LẠI",
                            value = "$wrongCount",
                            color = DuoColors.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Green DuoButton
                DuoButton(
                    onClick = onComplete,
                    backgroundColor = DuoColors.Green,
                    bottomColor = DuoColors.GreenDark,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Hoàn thành phiên học",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryStatCard3D(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: Color
) {
    DuoCard(
        modifier = modifier,
        backgroundColor = DuoColors.Background,
        borderColor = DuoColors.Border,
        shadowHeight = 3.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                color = DuoColors.TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = value,
                color = color,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
