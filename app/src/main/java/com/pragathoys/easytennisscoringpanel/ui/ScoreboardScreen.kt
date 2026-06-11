package com.pragathoys.easytennisscoringpanel.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pragathoys.easytennisscoringpanel.viewmodel.ScoreViewModel
import com.pragathoys.easytennisscoringpanel.domain.GameMode
import androidx.compose.runtime.LaunchedEffect
import com.pragathoys.easytennisscoringpanel.speech.SpeechManager
import com.pragathoys.easytennisscoringpanel.domain.buildAnnouncement


@Composable
fun ScoreboardScreen(vm: ScoreViewModel, speechManager: SpeechManager) {

    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        speechManager.speak(
            buildAnnouncement(state)
        )
    }

    Column(
        modifier = Modifier.padding(20.dp)
    ) {

        Text("🎾 EASY TENNIS PANEL", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        Text("Player A: ${state.aGames} games | ${state.aPoint}")
        Text("Player B: ${state.bGames} games | ${state.bPoint}")

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
    }
}