package com.example.maxlish.ui.screen.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.UserLevel

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            state = ProfileState(
                displayName = "Nguyễn Văn A",
                learningGoal = LearningGoal.COMMUNICATION,
                level = UserLevel.A2,
                isLoading = false,
                isSaveSuccess = false,
                errorMessage = null
            ),
            onEvent = {}
        )
    }
}