package com.example.maxlish.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ==========================================
// DUOLINGO COLOR PALETTE
// ==========================================
object DuoColors {
    val Green = Color(0xFF58CC02)
    val GreenDark = Color(0xFF58A700)
    
    val Blue = Color(0xFF1CB0F6)
    val BlueDark = Color(0xFF1899D6)
    
    val Orange = Color(0xFFFF9600)
    val OrangeDark = Color(0xFFE27D00)
    
    val Yellow = Color(0xFFFFC800)
    val YellowDark = Color(0xFFE6B400)
    
    val Red = Color(0xFFFF4B4B)
    val RedDark = Color(0xFFEA2B2B)
    
    val Purple = Color(0xFFCE82FF)
    val PurpleDark = Color(0xFFAA60EB)
    
    val White = Color(0xFFFFFFFF)
    val WhiteDark = Color(0xFFE5E5E5)
    
    val Border = Color(0xFFE5E5E5)
    val Background = Color(0xFFF7F7F7)
    
    val TextPrimary = Color(0xFF3C3C3C)
    val TextSecondary = Color(0xFF777777)
}

// ==========================================
// 3D DUOLINGO BUTTON
// ==========================================
@Composable
fun DuoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = DuoColors.Green,
    bottomColor: Color = DuoColors.GreenDark,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    shadowHeight: Dp = 4.dp,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val translationY by animateDpAsState(
        targetValue = if (isPressed && enabled) shadowHeight else 0.dp,
        animationSpec = tween(durationMillis = 60),
        label = "DuoButtonTranslationY"
    )

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Tắt default ripple để có cảm giác nhấn vật lý thật
                enabled = enabled,
                onClick = onClick
            )
            .padding(bottom = shadowHeight)
    ) {
        // Lớp Shadow ở dưới (Bóng đổ 3D)
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = shadowHeight)
                .background(
                    color = if (enabled) bottomColor else Color(0xFFBCC0C4),
                    shape = shape
                )
        )

        // Lớp Nút ở trên (Nút chính diện)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    this.translationY = translationY.toPx()
                }
                .background(
                    color = if (enabled) backgroundColor else Color(0xFFE5E5E5),
                    shape = shape
                )
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                content()
            }
        }
    }
}

// ==========================================
// 3D DUOLINGO CARD
// ==========================================
@Composable
fun DuoCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DuoColors.White,
    borderColor: Color = DuoColors.Border,
    shadowHeight: Dp = 4.dp,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        // Nếu Card click được, sử dụng hành vi lún nút 3D tương tự DuoButton
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val translationY by animateDpAsState(
            targetValue = if (isPressed) shadowHeight else 0.dp,
            animationSpec = tween(durationMillis = 60),
            label = "DuoCardTranslationY"
        )

        Box(
            modifier = modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
                .padding(bottom = shadowHeight)
        ) {
            // Shadow Layer
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = shadowHeight)
                    .background(
                        color = borderColor,
                        shape = shape
                    )
            )

            // Content Layer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        this.translationY = translationY.toPx()
                    }
                    .background(
                        color = backgroundColor,
                        shape = shape
                    )
            ) {
                content()
            }
        }
    } else {
        // Nếu Card không click được, chỉ hiển thị bóng đổ 3D tĩnh
        Box(
            modifier = modifier.padding(bottom = shadowHeight)
        ) {
            // Shadow Layer (Tĩnh)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = shadowHeight)
                    .background(
                        color = borderColor,
                        shape = shape
                    )
            )

            // Content Layer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = backgroundColor,
                        shape = shape
                    )
            ) {
                content()
            }
        }
    }
}

// ==========================================
// 3D DUOLINGO PROGRESS BAR
// ==========================================
@Composable
fun DuoProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = DuoColors.Green,
    trackColor: Color = DuoColors.Border,
    height: Dp = 16.dp
) {
    val clampedProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(100.dp))
            .background(trackColor)
    ) {
        if (clampedProgress > 0f) {
            // Lớp thanh tiến trình
            Box(
                modifier = Modifier
                    .fillMaxWidth(clampedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(100.dp))
                    .background(color)
            ) {
                // Vệt sáng 3D ở nửa trên thanh tiến trình
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.35f)
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                )
            }
        }
    }
}
