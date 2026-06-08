package com.example.maxlish.ui.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = viewModel(),
    onSaveSuccess: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    ProfileScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onSaveSuccess = onSaveSuccess,
        onLogout = onLogout
    )
}