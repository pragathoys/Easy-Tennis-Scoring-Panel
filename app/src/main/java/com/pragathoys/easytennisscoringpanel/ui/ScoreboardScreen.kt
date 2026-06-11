package com.pragathoys.easytennisscoringpanel.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pragathoys.easytennisscoringpanel.viewmodel.ScoreViewModel
import com.pragathoys.easytennisscoringpanel.domain.GameMode
import com.pragathoys.easytennisscoringpanel.speech.SpeechManager
import com.pragathoys.easytennisscoringpanel.domain.buildAnnouncement
import com.pragathoys.easytennisscoringpanel.domain.matchAnnouncement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreboardScreen(vm: ScoreViewModel, speechManager: SpeechManager, onOpenSettings: () -> Unit) {

    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state.speechEnabled) {
            speechManager.speak(
                buildAnnouncement(state) + " " + matchAnnouncement(state)
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎾 EASY TENNIS SCORING PANEL") },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
        ) {

            Spacer(Modifier.height(10.dp))

            val aPointDisplay = when (state.mode) {
                GameMode.ADV_A -> "Ad"
                GameMode.ADV_B -> "40"
                else -> state.aPoint.displayValue
            }

            val bPointDisplay = when (state.mode) {
                GameMode.ADV_B -> "Ad"
                GameMode.ADV_A -> "40"
                else -> state.bPoint.displayValue
            }

            Text("${state.playerAName}: ${state.aSets} Sets | ${state.aGames} Games | Score: $aPointDisplay")
            Text("${state.playerBName}: ${state.bSets} Sets | ${state.bGames} Games | Score: $bPointDisplay")

            Spacer(Modifier.height(10.dp))

            val statusText = when {
                state.matchOver -> "MATCH OVER - WINNER: ${state.winner}"
                state.inTiebreak -> "IN TIEBREAK"
                state.mode == GameMode.DEUCE -> "DEUCE"
                else -> "LIVE"
            }
            Text(text = "Status: $statusText", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(30.dp))

            Row {
                Button(onClick = { vm.pointA() }) {
                    Text("Point ${state.playerAName}")
                }

                Spacer(Modifier.width(10.dp))

                Button(onClick = { vm.pointB() }) {
                    Text("Point ${state.playerBName}")
                }
            }

            Spacer(Modifier.height(10.dp))

            Row {
                Button(onClick = { vm.undo() }) {
                    Text("UNDO")
                }

                Spacer(Modifier.width(10.dp))

                Button(onClick = { vm.reset() }) {
                    Text("RESET")
                }
            }

            Spacer(Modifier.height(10.dp))

            Button(onClick = {
                speechManager.speak("Welcome to the Easy Tennis Score Panel!!")
            }) {
                Text("Test Voice")
            }
        }
    }
}
