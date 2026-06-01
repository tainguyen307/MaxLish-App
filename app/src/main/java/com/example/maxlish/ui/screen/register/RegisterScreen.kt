package com.example.maxlish.ui.screen.register

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maxlish.R
import com.example.maxlish.ui.component.DuoButton
import com.example.maxlish.ui.component.DuoCard
import com.example.maxlish.ui.component.DuoColors

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onRegisterSuccess()
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Tiêu đề
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DuoColors.Blue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tạo tài khoản mới để bắt đầu học tập",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DuoColors.TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(36.dp))

            // Form Card
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
                    Text(
                        text = "Đăng ký",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    // Họ tên
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { viewModel.onEvent(RegisterEvent.NameChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Họ và tên", fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = DuoColors.TextSecondary)
                        },
                        singleLine = true,
                        isError = state.nameError != null,
                        supportingText = state.nameError?.let { { Text(stringResource(it), fontWeight = FontWeight.Bold) } },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DuoColors.Blue,
                            unfocusedBorderColor = DuoColors.Border,
                            errorBorderColor = DuoColors.Red,
                            focusedLabelColor = DuoColors.Blue,
                            unfocusedLabelColor = DuoColors.TextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email", fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = DuoColors.TextSecondary)
                        },
                        singleLine = true,
                        isError = state.emailError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        supportingText = state.emailError?.let { { Text(stringResource(it), fontWeight = FontWeight.Bold) } },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DuoColors.Blue,
                            unfocusedBorderColor = DuoColors.Border,
                            errorBorderColor = DuoColors.Red,
                            focusedLabelColor = DuoColors.Blue,
                            unfocusedLabelColor = DuoColors.TextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Mật khẩu
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = { viewModel.onEvent(RegisterEvent.PasswordChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Mật khẩu", fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = DuoColors.TextSecondary)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        isError = state.passwordError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        supportingText = state.passwordError?.let { { Text(stringResource(it), fontWeight = FontWeight.Bold) } },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DuoColors.Blue,
                            unfocusedBorderColor = DuoColors.Border,
                            errorBorderColor = DuoColors.Red,
                            focusedLabelColor = DuoColors.Blue,
                            unfocusedLabelColor = DuoColors.TextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Xác nhận mật khẩu
                    OutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = { viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Xác nhận mật khẩu", fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = DuoColors.TextSecondary)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        isError = state.confirmPasswordError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        supportingText = state.confirmPasswordError?.let { { Text(stringResource(it), fontWeight = FontWeight.Bold) } },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DuoColors.Blue,
                            unfocusedBorderColor = DuoColors.Border,
                            errorBorderColor = DuoColors.Red,
                            focusedLabelColor = DuoColors.Blue,
                            unfocusedLabelColor = DuoColors.TextSecondary
                        )
                    )

                    if (state.generalError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.generalError!!,
                            color = DuoColors.RedDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Nút Đăng ký 3D
                    DuoButton(
                        onClick = { viewModel.onEvent(RegisterEvent.RegisterClicked) },
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
                                text = "Đăng ký", 
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Link về Login
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đã có tài khoản?",
                    fontSize = 14.sp,
                    color = DuoColors.TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Đăng nhập",
                        color = DuoColors.Blue,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}
