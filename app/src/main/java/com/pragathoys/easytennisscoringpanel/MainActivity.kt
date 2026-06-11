package com.pragathoys.easytennisscoringpanel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pragathoys.easytennisscoringpanel.domain.MatchFormat
import com.pragathoys.easytennisscoringpanel.domain.TennisState
import com.pragathoys.easytennisscoringpanel.speech.SpeechManager
import com.pragathoys.easytennisscoringpanel.ui.ScoreboardScreen
import com.pragathoys.easytennisscoringpanel.viewmodel.ScoreViewModel

class MainActivity : ComponentActivity() {
    private lateinit var speechManager: SpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speechManager = SpeechManager(this)

        val prefs = getSharedPreferences("tennis_settings", Context.MODE_PRIVATE)

        val formatStr = intent.getStringExtra("MATCH_FORMAT") ?: prefs.getString("MATCH_FORMAT", MatchFormat.TWO_SETS_AND_SUPER_TIEBREAK.name) ?: MatchFormat.TWO_SETS_AND_SUPER_TIEBREAK.name
        val matchFormat = try { MatchFormat.valueOf(formatStr) } catch (e: Exception) { MatchFormat.TWO_SETS_AND_SUPER_TIEBREAK }
        
        val isDoubles = intent.getBooleanExtra("IS_DOUBLES", prefs.getBoolean("IS_DOUBLES", false))
        val playerA = intent.getStringExtra("PLAYER_A") ?: prefs.getString("PLAYER_A", "Player A") ?: "Player A"
        val playerB = intent.getStringExtra("PLAYER_B") ?: prefs.getString("PLAYER_B", "Player B") ?: "Player B"
        val speechEnabled = intent.getBooleanExtra("SPEECH_ENABLED", prefs.getBoolean("SPEECH_ENABLED", true))

        val initialState = TennisState(
            matchFormat = matchFormat,
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
