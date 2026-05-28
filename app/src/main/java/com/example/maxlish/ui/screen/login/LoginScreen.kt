package com.example.maxlish.ui.screen.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maxlish.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { idToken ->
                    viewModel.onEvent(LoginEvent.GoogleIdTokenReceived(idToken))
                }
            } catch (e: ApiException) {
                // Xử lý lỗi Google Sign-In
            }
        }
    }

    // Navigate khi login thành công
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Logo / Tiêu đề
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.slogan),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(40.dp))

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Đăng nhập",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Email field
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        },
                        singleLine = true,
                        isError = state.emailError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        supportingText = state.emailError?.let { { Text(it) } }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password field
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Mật khẩu") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        isError = state.passwordError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        supportingText = state.passwordError?.let { { Text(it) } }
                    )

                    // Lỗi chung từ server
                    if (state.generalError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.generalError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Nút Đăng nhập
                    Button(
                        onClick = { viewModel.onEvent(LoginEvent.LoginClicked) },
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
                            Text("Đăng nhập", style = MaterialTheme.typography.labelLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            text = "  hoặc  ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nút Google Sign-In
                    OutlinedButton(
                        onClick = {
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken("954231504710-h7mnbcqpsl9ph1fg5lc6rci99rcpoltq.apps.googleusercontent.com") // Thay bằng Web Client ID từ Firebase Console
                                .requestEmail()
                                .build()
                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            googleSignInLauncher.launch(googleSignInClient.signInIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !state.isLoading,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_search),
                            contentDescription = "Google",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tiếp tục với Google")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Link đến Register
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chưa có tài khoản?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text("Đăng ký ngay")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}