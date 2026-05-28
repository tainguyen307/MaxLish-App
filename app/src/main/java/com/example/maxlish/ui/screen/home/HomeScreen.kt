package com.example.maxlish.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Primary = Color(0xFF2563EB)
private val Secondary = Color(0xFF22C55E)
private val Orange = Color(0xFFFF9800)

private val Background = Color(0xFFF8FAFC)
private val CardColor = Color.White

private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF64748B)

data class Course(
    val title: String,
    val words: Int,
    val progress: Float,
    val icon: ImageVector,
    val color: Color
)

data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

    val selectedBottomNav = remember {
        mutableIntStateOf(0)
    }

    val courses = listOf(
        Course(
            title = "IELTS Vocabulary",
            words = 840,
            progress = 0.75f,
            icon = Icons.Default.MenuBook,
            color = Primary
        ),
        Course(
            title = "Daily Communication",
            words = 250,
            progress = 0.35f,
            icon = Icons.Default.Chat,
            color = Secondary
        ),
        Course(
            title = "Business English",
            words = 1200,
            progress = 0.1f,
            icon = Icons.Default.Work,
            color = Orange
        )
    )

    val quickActions = listOf(
        QuickAction(
            title = "Vocabulary",
            icon = Icons.Default.Book,
            color = Primary
        ),
        QuickAction(
            title = "Speaking",
            icon = Icons.Default.GraphicEq,
            color = Secondary
        ),
        QuickAction(
            title = "Listening",
            icon = Icons.Default.Headphones,
            color = Orange
        ),
        QuickAction(
            title = "Grammar",
            icon = Icons.Default.Analytics,
            color = Color(0xFF8B5CF6)
        )
    )

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
                HomeTopBar()
            }

            item {
                ContinueLearningCard()
            }

            item {
                ReviewDueCard()
            }

            item {
                SectionTitle(
                    title = "Quick Practice"
                )
            }

            item {
                QuickActionGrid(
                    actions = quickActions
                )
            }

            item {
                SectionTitle(
                    title = "Your Courses"
                )
            }

            item {
                CourseSection(
                    courses = courses
                )
            }

            item {
                DailyChallengeCard()
            }

            item {
                WeeklyActivityCard()
            }

            item {
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }
    }
}

@Composable
fun HomeTopBar() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        Column {

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tai",
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
                    text = "15",
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
fun ContinueLearningCard() {

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
                text = "IELTS Vocabulary",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Lesson 12 • 25 words remaining",
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            LinearProgressIndicator(
                progress = { 0.5f },
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
                    value = "+120"
                )

                HeroStat(
                    title = "Accuracy",
                    value = "92%"
                )

                HeroStat(
                    title = "Review",
                    value = "42"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
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
fun ReviewDueCard() {

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
                    text = "42 cards waiting",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Button(
                onClick = {},
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Review")
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String
) {

    Text(
        text = title,
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
}

@Composable
fun QuickActionGrid(
    actions: List<QuickAction>
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            actions.take(2).forEach {
                QuickActionCard(action = it)
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            actions.drop(2).forEach {
                QuickActionCard(action = it)
            }
        }
    }
}

@Composable
fun RowScope.QuickActionCard(
    action: QuickAction
) {

    Card(
        modifier = Modifier
            .weight(1F)
            .height(120.dp)
            .clickable { },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(action.color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = action.icon,
                    contentDescription = null,
                    tint = action.color
                )
            }

            Text(
                text = action.title,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun CourseSection(
    courses: List<Course>
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(courses) { course ->

            CourseCard(
                course = course
            )
        }
    }
}

@Composable
fun CourseCard(
    course: Course
) {

    Card(
        modifier = Modifier
            .width(260.dp)
            .clickable { },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(course.color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = course.icon,
                    contentDescription = null,
                    tint = course.color
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = course.title,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${course.words} words",
                color = TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { course.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(100.dp)),
                color = course.color,
                trackColor = course.color.copy(alpha = 0.12f)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = course.color
                ),
                shape = RoundedCornerShape(16.dp)
            ) {

                Text(
                    text = "Continue"
                )
            }
        }
    }
}

@Composable
fun DailyChallengeCard() {

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF7ED)
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Text(
                text = "Daily Challenge 🔥",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Complete a 5-minute challenge and earn 50 XP.",
                color = TextSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                )
            ) {

                Text(
                    text = "Start Challenge"
                )
            }
        }
    }
}

@Composable
fun WeeklyActivityCard() {

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Text(
                text = "Weekly Activity",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                val values = listOf(
                    40,
                    65,
                    30,
                    90,
                    55,
                    70,
                    100
                )

                val days = listOf(
                    "M",
                    "T",
                    "W",
                    "T",
                    "F",
                    "S",
                    "S"
                )

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