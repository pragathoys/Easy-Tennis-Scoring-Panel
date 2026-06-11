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

            Text("${state.playerAName}: ${state.aSets} set | ${state.aGames} games | ${state.aPoint}")
            Text("${state.playerBName}: ${state.bSets} set | ${state.bGames} games | ${state.bPoint}")

            Text(
                text = when (state.mode) {
                    GameMode.DEUCE -> "DEUCE"
                    GameMode.ADV_A -> "ADVANTAGE A"
                    GameMode.ADV_B -> "ADVANTAGE B"
                    else -> ""
                }
            )
            Text(
                text = when (state.mode) {
                    GameMode.NORMAL -> "normal"
                    GameMode.DEUCE -> "DEUCE"
                    GameMode.ADV_A -> "ADVANTAGE A"
                    GameMode.ADV_B -> "ADVANTAGE B"
                    else -> state.mode.name
                }
            )
            Text(
                text = when (state.inTiebreak) {
                    state.inTiebreak -> "In Tiebreak!!"
                    else -> ""
                }
            )

            Text(
                text = when {
                    state.matchOver -> "MATCH OVER"
                    else -> "LIVE"
                }
            )

            Spacer(Modifier.height(30.dp))

            Row {
                Button(onClick = { vm.pointA() }) {
                    Text("+ A")
                }

                Spacer(Modifier.width(10.dp))

                Button(onClick = { vm.pointB() }) {
                    Text("+ B")
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

            Row {
                Button(onClick = {
                    speechManager.speak("Welcome to the Easy Tennis Score Panel!!")
                }) {
                    Text("Test Voice")
                }
            }
        }
    }
}
