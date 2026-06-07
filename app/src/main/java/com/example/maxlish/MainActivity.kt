package com.example.maxlish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.maxlish.data.helper.NotificationHelper
import com.example.maxlish.data.helper.NotificationScheduler
import com.example.maxlish.data.repository.FirebaseAuthRepository
import com.example.maxlish.ui.navigation.AppNavGraph
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createChannel(this)

        val currentUser = FirebaseAuthRepository().getCurrentUser()

        if (currentUser != null) {
            NotificationScheduler.schedule(this, currentUser.uid)
        }

        setContent {
            AppNavGraph()
        }
    }
}
