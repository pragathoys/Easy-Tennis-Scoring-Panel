package com.pragathoys.easytennisscoringpanel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pragathoys.easytennisscoringpanel.ui.ScoreboardScreen
import com.pragathoys.easytennisscoringpanel.viewmodel.ScoreViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val vm: ScoreViewModel = viewModel()
            ScoreboardScreen(vm)
        }
    }
}