package com.example.maxlish.ui.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = viewModel(),
    onSaveSuccess: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    var goalExpanded by remember { mutableStateOf(false) }
    var levelExpanded by remember { mutableStateOf(false) }

    ProfileScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onSaveSuccess = onSaveSuccess,
        onLogout = onLogout
    )
}