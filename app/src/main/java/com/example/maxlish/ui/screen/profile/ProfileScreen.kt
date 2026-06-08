package com.example.maxlish.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.UserLevel
import com.example.maxlish.ui.component.DuoButton
import com.example.maxlish.ui.component.DuoCard
import com.example.maxlish.ui.component.DuoColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    onSaveSuccess: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var goalExpanded by remember { mutableStateOf(false) }
    var levelExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSaveSuccess) {
        if (state.isSaveSuccess) {
            onEvent(ProfileEvent.SaveSuccessConsumed)
            onSaveSuccess()
        }
    }

    Scaffold(
        containerColor = DuoColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Avatar Container
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(DuoColors.Blue.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(84.dp),
                    tint = DuoColors.Blue
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            
            Text(
                text = "Hồ sơ cá nhân",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DuoColors.TextPrimary
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Text(
                text = "Thiết lập mục tiêu và trình độ học tập",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DuoColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Form 3D Card
            DuoCard(
                backgroundColor = DuoColors.White,
                borderColor = DuoColors.Border,
                shadowHeight = 5.dp,
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Tên hiển thị
                    OutlinedTextField(
                        value = state.displayName,
                        onValueChange = { onEvent(ProfileEvent.NameChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Tên hiển thị", fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = DuoColors.TextSecondary)
                        },
                        singleLine = true,
                        isError = state.errorMessage != null,
                        supportingText = state.errorMessage?.let { { Text(it, fontWeight = FontWeight.Bold) } },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DuoColors.Blue,
                            unfocusedBorderColor = DuoColors.Border,
                            errorBorderColor = DuoColors.Red,
                            focusedLabelColor = DuoColors.Blue,
                            unfocusedLabelColor = DuoColors.TextSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mục tiêu học – Dropdown
                    ExposedDropdownMenuBox(
                        expanded = goalExpanded,
                        onExpandedChange = { goalExpanded = !goalExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.learningGoal.displayName,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                            label = { Text("Mục tiêu học", fontWeight = FontWeight.Bold) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = goalExpanded)
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DuoColors.Blue,
                                unfocusedBorderColor = DuoColors.Border,
                                focusedLabelColor = DuoColors.Blue,
                                unfocusedLabelColor = DuoColors.TextSecondary
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = goalExpanded,
                            onDismissRequest = { goalExpanded = false }
                        ) {
                            LearningGoal.entries.forEach { goal ->
                                DropdownMenuItem(
                                    text = { Text(goal.displayName, fontWeight = FontWeight.Bold, color = DuoColors.TextPrimary) },
                                    onClick = {
                                        onEvent(ProfileEvent.GoalChanged(goal))
                                        goalExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Level – Dropdown
                    ExposedDropdownMenuBox(
                        expanded = levelExpanded,
                        onExpandedChange = { levelExpanded = !levelExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.level.name,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                            label = { Text("Trình độ hiện tại", fontWeight = FontWeight.Bold) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded)
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DuoColors.Blue,
                                unfocusedBorderColor = DuoColors.Border,
                                focusedLabelColor = DuoColors.Blue,
                                unfocusedLabelColor = DuoColors.TextSecondary
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = levelExpanded,
                            onDismissRequest = { levelExpanded = false }
                        ) {
                            UserLevel.entries.forEach { lvl ->
                                DropdownMenuItem(
                                    text = { Text(lvl.name, fontWeight = FontWeight.Bold, color = DuoColors.TextPrimary) },
                                    onClick = {
                                        onEvent(ProfileEvent.LevelChanged(lvl))
                                        levelExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Nút Lưu 3D Green
                    DuoButton(
                        onClick = { onEvent(ProfileEvent.SaveClicked) },
                        backgroundColor = DuoColors.Green,
                        bottomColor = DuoColors.GreenDark,
                        enabled = !state.isLoading,
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
                                text = "Lưu hồ sơ", 
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Nút Đăng xuất 3D White (Border/Text Red)
                    DuoButton(
                        onClick = {
                            onEvent(ProfileEvent.LogoutClicked)
                            onLogout()
                        },
                        backgroundColor = DuoColors.White,
                        bottomColor = DuoColors.WhiteDark,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Đăng xuất", 
                            color = DuoColors.Red,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}
