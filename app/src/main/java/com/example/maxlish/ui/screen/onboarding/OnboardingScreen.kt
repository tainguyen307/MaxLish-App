package com.example.maxlish.ui.screen.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.UserLevel
import com.example.maxlish.ui.component.DuoButton
import com.example.maxlish.ui.component.DuoColors

// ============================================================
// ONBOARDING SCREEN — Wizard 3 bước sau khi đăng ký
// ============================================================

@OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onEvent: (OnboardingEvent) -> Unit,
    onNavigateToHome: () -> Unit
) {
    LaunchedEffect(state.isDone) {
        if (state.isDone) onNavigateToHome()
    }

    val progress by animateFloatAsState(
        targetValue = state.step / 3f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "progress"
    )

    Scaffold(containerColor = DuoColors.Background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Top bar: back + progress ─────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Nút back (ẩn ở step 1)
                AnimatedVisibility(visible = state.step > 1) {
                    IconButton(
                        onClick = { onEvent(OnboardingEvent.PrevStep) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = DuoColors.TextPrimary
                        )
                    }
                }
                if (state.step == 1) Spacer(modifier = Modifier.size(40.dp))

                // Progress bar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(DuoColors.Border)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(100.dp))
                            .background(DuoColors.Blue)
                    )
                }

                // Step counter
                Text(
                    text = "${state.step}/3",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = DuoColors.TextSecondary,
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.End
                )
            }

            // ── Content (animated slide) ─────────────────────────
            AnimatedContent(
                targetState = state.step,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally(tween(300)) { it } + fadeIn(tween(300)) togetherWith
                            slideOutHorizontally(tween(300)) { -it } + fadeOut(tween(300))
                    } else {
                        slideInHorizontally(tween(300)) { -it } + fadeIn(tween(300)) togetherWith
                            slideOutHorizontally(tween(300)) { it } + fadeOut(tween(300))
                    }
                },
                label = "step_content",
                modifier = Modifier.weight(1f)
            ) { step ->
                when (step) {
                    1 -> StepGoal(
                        selected = state.selectedGoal,
                        onSelect = { onEvent(OnboardingEvent.GoalSelected(it)) }
                    )
                    2 -> StepLevel(
                        selected = state.selectedLevel,
                        onSelect = { onEvent(OnboardingEvent.LevelSelected(it)) }
                    )
                    3 -> StepDailyWords(
                        selected = state.dailyWords,
                        onSelect = { onEvent(OnboardingEvent.DailyWordsSelected(it)) }
                    )
                }
            }

            // ── Bottom button ─────────────────────────────────────
            val canProceed = when (state.step) {
                1 -> state.selectedGoal != null
                2 -> state.selectedLevel != null
                else -> true
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage,
                        color = DuoColors.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }

                DuoButton(
                    onClick = {
                        if (state.step < 3) onEvent(OnboardingEvent.NextStep)
                        else onEvent(OnboardingEvent.Finish)
                    },
                    backgroundColor = if (canProceed) DuoColors.Green else DuoColors.Border,
                    bottomColor = if (canProceed) DuoColors.GreenDark else DuoColors.WhiteDark,
                    enabled = canProceed && !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text(
                            text = if (state.step < 3) "Tiếp theo →" else "🎉  Bắt đầu học!",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// STEP 1 — Chọn mục tiêu học
// ─────────────────────────────────────────────────────────────
@Composable
private fun StepGoal(
    selected: LearningGoal?,
    onSelect: (LearningGoal) -> Unit
) {
    val goals = listOf(
        Triple(LearningGoal.IELTS, "🎓", "Thi IELTS"),
        Triple(LearningGoal.TOEIC, "📋", "Thi TOEIC"),
        Triple(LearningGoal.COMMUNICATION, "💬", "Giao tiếp\nhàng ngày"),
        Triple(LearningGoal.BUSINESS, "💼", "Tiếng Anh\nThương mại"),
        Triple(LearningGoal.TRAVEL, "✈️", "Du lịch\nnước ngoài"),
        Triple(LearningGoal.ACADEMIC, "📚", "Học thuật\n& Nghiên cứu"),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Mục tiêu của bạn là gì?", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = DuoColors.TextPrimary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Chọn mục tiêu để chúng tôi cá nhân hoá lộ trình học", fontSize = 14.sp, color = DuoColors.TextSecondary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))

        // 2 cột x 3 hàng
        for (row in goals.chunked(2)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { (goal, emoji, label) ->
                    val isSelected = selected == goal
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) DuoColors.Blue.copy(alpha = 0.12f)
                                else DuoColors.White
                            )
                            .border(
                                width = if (isSelected) 2.5.dp else 1.5.dp,
                                color = if (isSelected) DuoColors.Blue else DuoColors.Border,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onSelect(goal) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(emoji, fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                label,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isSelected) DuoColors.Blue else DuoColors.TextPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                // Nếu hàng lẻ, thêm spacer cho cân
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// ─────────────────────────────────────────────────────────────
// STEP 2 — Chọn trình độ
// ─────────────────────────────────────────────────────────────
@Composable
private fun StepLevel(
    selected: UserLevel?,
    onSelect: (UserLevel) -> Unit
) {
    val levels = listOf(
        Triple(UserLevel.A1, "Mới bắt đầu", "Chưa biết gì về tiếng Anh"),
        Triple(UserLevel.A2, "Sơ cấp", "Biết những cụm từ thông dụng"),
        Triple(UserLevel.B1, "Trung cấp", "Có thể giao tiếp cơ bản"),
        Triple(UserLevel.B2, "Trên trung cấp", "Giao tiếp tốt về nhiều chủ đề"),
        Triple(UserLevel.C1, "Nâng cao", "Diễn đạt lưu loát, tự nhiên"),
        Triple(UserLevel.C2, "Thành thạo", "Gần như người bản ngữ"),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Trình độ hiện tại?", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = DuoColors.TextPrimary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Chúng tôi sẽ điều chỉnh độ khó phù hợp với bạn", fontSize = 14.sp, color = DuoColors.TextSecondary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))

        levels.forEach { (level, name, desc) ->
            val isSelected = selected == level
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        if (isSelected) DuoColors.Blue.copy(alpha = 0.1f) else DuoColors.White
                    )
                    .border(
                        width = if (isSelected) 2.dp else 1.5.dp,
                        color = if (isSelected) DuoColors.Blue else DuoColors.Border,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable { onSelect(level) }
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Badge trình độ
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) DuoColors.Blue else DuoColors.Border.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = if (isSelected) Color.White else DuoColors.TextSecondary
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = if (isSelected) DuoColors.Blue else DuoColors.TextPrimary
                    )
                    Text(
                        text = desc,
                        fontSize = 12.sp,
                        color = DuoColors.TextSecondary
                    )
                }
                if (isSelected) {
                    Text("✓", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = DuoColors.Blue)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// ─────────────────────────────────────────────────────────────
// STEP 3 — Số từ mục tiêu mỗi ngày
// ─────────────────────────────────────────────────────────────
@Composable
private fun StepDailyWords(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    val options = listOf(
        Triple(5,  "😌", "Nhẹ nhàng"),
        Triple(10, "🙂", "Thoải mái"),
        Triple(20, "💪", "Vừa phải"),
        Triple(30, "🔥", "Quyết tâm"),
        Triple(50, "🚀", "Siêu tốc"),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Mục tiêu mỗi ngày?", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = DuoColors.TextPrimary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Bạn muốn học bao nhiêu từ mỗi ngày?", fontSize = 14.sp, color = DuoColors.TextSecondary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(28.dp))

        options.forEach { (count, emoji, label) ->
            val isSelected = selected == count
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        when {
                            isSelected && count >= 30 -> DuoColors.Orange.copy(alpha = 0.1f)
                            isSelected -> DuoColors.Green.copy(alpha = 0.1f)
                            else -> DuoColors.White
                        }
                    )
                    .border(
                        width = if (isSelected) 2.dp else 1.5.dp,
                        color = when {
                            isSelected && count >= 30 -> DuoColors.Orange
                            isSelected -> DuoColors.Green
                            else -> DuoColors.Border
                        },
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable { onSelect(count) }
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(emoji, fontSize = 28.sp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$count từ / ngày",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = when {
                            isSelected && count >= 30 -> DuoColors.Orange
                            isSelected -> DuoColors.Green
                            else -> DuoColors.TextPrimary
                        }
                    )
                    Text(
                        text = label,
                        fontSize = 13.sp,
                        color = DuoColors.TextSecondary
                    )
                }
                if (isSelected) {
                    Text(
                        "✓",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = if (count >= 30) DuoColors.Orange else DuoColors.Green
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
