package com.pragathoys.easytennisscoringpanel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pragathoys.easytennisscoringpanel.domain.TennisState
import com.pragathoys.easytennisscoringpanel.speech.SpeechManager
import com.pragathoys.easytennisscoringpanel.ui.ScoreboardScreen
import com.pragathoys.easytennisscoringpanel.viewmodel.ScoreViewModel

class MainActivity : ComponentActivity() {
    private lateinit var speechManager: SpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speechManager = SpeechManager(this)

        // Try to get from Intent (passed from SettingsActivity)
        // If not present (e.g. app restart), fallback to SharedPreferences
        val prefs = getSharedPreferences("tennis_settings", Context.MODE_PRIVATE)

        val bestOf = intent.getIntExtra("BEST_OF", prefs.getInt("BEST_OF", 3))
        val isDoubles = intent.getBooleanExtra("IS_DOUBLES", prefs.getBoolean("IS_DOUBLES", false))
        val playerA = intent.getStringExtra("PLAYER_A") ?: prefs.getString("PLAYER_A", "Player A") ?: "Player A"
        val playerB = intent.getStringExtra("PLAYER_B") ?: prefs.getString("PLAYER_B", "Player B") ?: "Player B"
        val speechEnabled = intent.getBooleanExtra("SPEECH_ENABLED", prefs.getBoolean("SPEECH_ENABLED", true))

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
                speechManager = speechManager,
                onOpenSettings = {
                    val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }

    override fun onDestroy() {
        speechManager.shutdown()
        super.onDestroy()
    }
}
