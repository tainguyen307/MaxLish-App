package com.example.maxlish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.maxlish.ui.navigation.AppNavGraph
import androidx.lifecycle.lifecycleScope
import com.example.maxlish.data.seed.SeedData
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavGraph()
        }
    }
}
