package com.example.maxlish.ui.screen.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel

private val Primary = Color(0xFF2563EB)
private val Secondary = Color(0xFF22C55E)
private val Orange = Color(0xFFFF9800)

private val Background = Color(0xFFF8FAFC)
private val CardColor = Color.White

private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF64748B)

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

    Scaffold(
        containerColor = Background
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = 20.dp,
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
                        state.currentVocabularySetId?.let { id ->
                            onEvent(HomeEvent.OnVocabularySetClick(id))
                        }
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
        verticalAlignment = Alignment.Top
    ) {

        Column {

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = userName,
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0xFFFFF3E0))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = Orange,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = streak.toString(),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(CardColor),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = TextPrimary
                )
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

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Primary,
                            Color(0xFF1D4ED8)
                        )
                    )
                )
                .padding(24.dp)
        ) {

            Text(
                text = "Continue Learning",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$remainingWords words remaining",
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(100.dp)),
                color = Secondary,
                trackColor = Color.White.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                HeroStat(
                    title = "XP",
                    value = "+$earnedXp"
                )

                HeroStat(
                    title = "Accuracy",
                    value = "$accuracy%"
                )

                HeroStat(
                    title = "Review",
                    value = reviewCount.toString()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onContinueClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Primary
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Continue",
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HeroStat(
    title: String,
    value: String
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.14f))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {

        Text(
            text = title,
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ReviewDueCard(
    count: Int,
    onReviewClick: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "Review Due",
                    color = TextSecondary,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$count cards waiting",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Button(
                onClick = onReviewClick,
                shape = RoundedCornerShape(16.dp)
            ) {

                Text("Review")
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {

    Text(
        text = title,
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
}

@Composable
fun VocabularySetSection(
    sets: List<VocabularySetUiModel>,
    onSetClick: (String) -> Unit
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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

    Card(
        modifier = Modifier
            .width(260.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = vocabularySet.title,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${vocabularySet.totalWords} words",
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { vocabularySet.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(100.dp)),
                color = Primary,
                trackColor = Primary.copy(alpha = 0.12f)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Learn")
            }
        }
    }
}

@Composable
fun WeeklyActivityCard(

    values: List<Int>,

    onClick: () -> Unit
) {

    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Weekly Activity",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Text(
                    text = "See Details",
                    color = Primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                val days =
                    listOf("M", "T", "W", "T", "F", "S", "S")

                values.forEachIndexed { index, value ->

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {

                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(value.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 12.dp,
                                        topEnd = 12.dp
                                    )
                                )
                                .background(
                                    if (index == values.lastIndex)
                                        Primary
                                    else
                                        Primary.copy(alpha = 0.22f)
                                )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = days[index],
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}