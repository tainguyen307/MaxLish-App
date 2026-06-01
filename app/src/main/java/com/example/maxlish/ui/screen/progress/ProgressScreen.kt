package com.example.maxlish.ui.screen.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.data.model.StudySession

private val Primary = Color(0xFF2563EB)
private val Secondary = Color(0xFF22C55E)
private val Orange = Color(0xFFFF9800)
private val Background = Color(0xFFF8FAFC)
private val CardColor = Color.White
private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Progress Tracking",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            }
        } else {
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
                // 3.4.1 Dashboard
                item {
                    SectionTitle(title = "Dashboard")
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Words",
                            value = "${uiState.user?.totalWordsLearned ?: 0}",
                            icon = Icons.AutoMirrored.Filled.MenuBook,
                            iconColor = Primary
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Streak",
                            value = "${uiState.user?.streak ?: 0} Days",
                            icon = Icons.Default.LocalFireDepartment,
                            iconColor = Orange
                        )
                    }
                }

                item {
                    StatCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Accuracy Rate",
                        value = "%.1f%%".format(viewModel.getAccuracy()),
                        icon = Icons.Default.EmojiEvents,
                        iconColor = Secondary
                    )
                }

                // 3.4.3 Level estimation
                item {
                    SectionTitle(title = "Level Estimation")
                }

                item {
                    LevelCard(
                        level = uiState.user?.level?.name ?: "A1",
                        category = viewModel.getLevelCategory()
                    )
                }

                // 3.4.2 Biểu đồ
                item {
                    SectionTitle(title = "Daily Activity")
                }

                item {
                    DailyActivityChart(uiState.weeklyActivity)
                }

                item {
                    SectionTitle(title = "Recent Sessions")
                }

                if (uiState.studySessions.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = CardColor)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No study sessions yet. Start a learning session to see progress.",
                                    color = TextSecondary,
                                    modifier = Modifier.padding(24.dp)
                                )
                            }
                        }
                    }
                } else {
                    items(uiState.studySessions.takeLast(4).reversed()) { session ->
                        SessionCard(session = session)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun SessionCard(session: StudySession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
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
                    text = formatSessionDate(session.startedAt),
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${session.reviewedWords} words reviewed",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${session.correctAnswers} correct · ${session.wrongAnswers} wrong",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${session.durationMinutes} min",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Primary.copy(alpha = 0.14f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (session.correctAnswers + session.wrongAnswers == 0) "N/A"
                        else "${((session.correctAnswers.toFloat() / (session.correctAnswers + session.wrongAnswers)) * 100).toInt()}%",
                        color = Primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun formatSessionDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
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
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
            Column {
                Text(
                    text = title,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = value,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LevelCard(level: String, category: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Primary
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = "Current Level: $level",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Category: $category",
                    color = TextSecondary,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun DailyActivityChart(
    values: List<Int>
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
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
                                .height(
                                    value
                                        .coerceAtLeast(8)
                                        .dp
                                )
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

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = days[index],
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}