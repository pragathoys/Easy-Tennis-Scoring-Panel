package com.pragathoys.easytennisscoringpanel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pragathoys.easytennisscoringpanel.ui.ScoreboardScreen
import com.pragathoys.easytennisscoringpanel.viewmodel.ScoreViewModel
import com.pragathoys.easytennisscoringpanel.speech.SpeechManager

import com.pragathoys.easytennisscoringpanel.domain.TennisState

class MainActivity : ComponentActivity() {
    private lateinit var speechManager: SpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speechManager = SpeechManager(this)

        val bestOf = intent.getIntExtra("BEST_OF", 3)
        val isDoubles = intent.getBooleanExtra("IS_DOUBLES", false)
        val playerA = intent.getStringExtra("PLAYER_A") ?: "Player A"
        val playerB = intent.getStringExtra("PLAYER_B") ?: "Player B"
        val speechEnabled = intent.getBooleanExtra("SPEECH_ENABLED", true)

        val initialState = TennisState(
            bestOfSets = bestOf,
            isDoubles = isDoubles,
            playerAName = playerA,
            playerBName = playerB,
            speechEnabled = speechEnabled
        )

        setContent {
            val vm: ScoreViewModel = viewModel()
            LaunchedEffect(Unit) {
                vm.setInitialState(initialState)
            }
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