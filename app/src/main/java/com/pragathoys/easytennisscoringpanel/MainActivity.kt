package com.pragathoys.easytennisscoringpanel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pragathoys.easytennisscoringpanel.ui.ScoreboardScreen
import com.pragathoys.easytennisscoringpanel.viewmodel.ScoreViewModel
import com.pragathoys.easytennisscoringpanel.speech.SpeechManager

class MainActivity : ComponentActivity() {
    private lateinit var speechManager: SpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speechManager = SpeechManager(this)

        setContent {
            val vm: ScoreViewModel = viewModel()
            ScoreboardScreen(
                vm = vm,
                speechManager = speechManager
            )
        }
    }

    override fun onDestroy() {
        speechManager.shutdown()
        super.onDestroy()
    }
}