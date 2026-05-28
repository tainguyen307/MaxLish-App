package com.example.maxlish.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.UserLevel

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
            onSaveSuccess()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Avatar
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar",
                modifier = Modifier.size(96.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hồ sơ của bạn",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Thiết lập mục tiêu học để bắt đầu",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
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
                        label = { Text("Tên hiển thị") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        singleLine = true,
                        isError = state.errorMessage != null,
                        supportingText = state.errorMessage?.let { { Text(it) } }
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
                            label = { Text("Mục tiêu học") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = goalExpanded)
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = goalExpanded,
                            onDismissRequest = { goalExpanded = false }
                        ) {
                            LearningGoal.entries.forEach { goal ->
                                DropdownMenuItem(
                                    text = { Text(goal.displayName) },
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
                            label = { Text("Trình độ hiện tại") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded)
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = levelExpanded,
                            onDismissRequest = { levelExpanded = false }
                        ) {
                            UserLevel.entries.forEach { lvl ->
                                DropdownMenuItem(
                                    text = { Text(lvl.name) },
                                    onClick = {
                                        onEvent(ProfileEvent.LevelChanged(lvl))
                                        levelExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nút Lưu
                    Button(
                        onClick = { onEvent(ProfileEvent.SaveClicked) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Lưu hồ sơ", style = MaterialTheme.typography.labelLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nút Đăng xuất
                    OutlinedButton(
                        onClick = {
                            onEvent(ProfileEvent.LogoutClicked)
                            onLogout()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Đăng xuất", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
