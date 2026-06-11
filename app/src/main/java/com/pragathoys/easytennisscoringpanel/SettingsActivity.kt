package com.pragathoys.easytennisscoringpanel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pragathoys.easytennisscoringpanel.domain.MatchFormat
import com.pragathoys.easytennisscoringpanel.speech.SpeechManager

class SettingsActivity : ComponentActivity() {
    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("tennis_settings", Context.MODE_PRIVATE)
    }

    private lateinit var speechManager: SpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speechManager = SpeechManager(this)
        
        setContent {
            MaterialTheme {
                SettingsScreen(
                    prefs = prefs,
                    speechManager = speechManager,
                    onStartMatch = { format, isDoubles, nameA, nameB, speechEnabled ->
                        // Save to persistent storage
                        prefs.edit().apply {
                            putString("MATCH_FORMAT", format.name)
                            putBoolean("IS_DOUBLES", isDoubles)
                            putString("PLAYER_A", nameA)
                            putString("PLAYER_B", nameB)
                            putBoolean("SPEECH_ENABLED", speechEnabled)
                            apply()
                        }

                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("MATCH_FORMAT", format.name)
                            putExtra("IS_DOUBLES", isDoubles)
                            putExtra("PLAYER_A", nameA)
                            putExtra("PLAYER_B", nameB)
                            putExtra("SPEECH_ENABLED", speechEnabled)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        speechManager.shutdown()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    prefs: SharedPreferences, 
    speechManager: SpeechManager,
    onStartMatch: (MatchFormat, Boolean, String, String, Boolean) -> Unit
) {
    var matchFormat by remember { 
        mutableStateOf(MatchFormat.valueOf(prefs.getString("MATCH_FORMAT", MatchFormat.TWO_SETS_AND_SUPER_TIEBREAK.name) ?: MatchFormat.TWO_SETS_AND_SUPER_TIEBREAK.name)) 
    }
    var isDoubles by remember { mutableStateOf(prefs.getBoolean("IS_DOUBLES", false)) }
    var playerAName by remember { mutableStateOf(prefs.getString("PLAYER_A", "Player A") ?: "Player A") }
    var playerBName by remember { mutableStateOf(prefs.getString("PLAYER_B", "Player B") ?: "Player B") }
    var speechEnabled by remember { mutableStateOf(prefs.getBoolean("SPEECH_ENABLED", true)) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Match Configuration") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Match Format", style = MaterialTheme.typography.titleMedium)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = matchFormat == MatchFormat.TWO_SETS_AND_SUPER_TIEBREAK, onClick = { matchFormat = MatchFormat.TWO_SETS_AND_SUPER_TIEBREAK })
                    Text("2 Sets + Super Tiebreak (10 pts)")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = matchFormat == MatchFormat.BEST_OF_3, onClick = { matchFormat = MatchFormat.BEST_OF_3 })
                    Text("Best of 3 Sets")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = matchFormat == MatchFormat.BEST_OF_5, onClick = { matchFormat = MatchFormat.BEST_OF_5 })
                    Text("Best of 5 Sets")
                }
            }

            Text("Match Type", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = !isDoubles, onClick = { isDoubles = false })
                Text("Singles")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = isDoubles, onClick = { isDoubles = true })
                Text("Doubles")
            }

            OutlinedTextField(
                value = playerAName,
                onValueChange = { playerAName = it },
                label = { Text("Player/Team A Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = playerBName,
                onValueChange = { playerBName = it },
                label = { Text("Player/Team B Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = speechEnabled, onCheckedChange = { speechEnabled = it })
                Text("Enable Score Announcements")
            }

            Button(
                onClick = { 
                    speechManager.speak("Welcome to the Easy Tennis Score Panel!!")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Test Voice")
            }

            Button(
                onClick = { onStartMatch(matchFormat, isDoubles, playerAName, playerBName, speechEnabled) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Match")
            }
        }
    }
}
