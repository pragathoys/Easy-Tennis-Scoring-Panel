package com.pragathoys.easytennisscoringpanel.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pragathoys.easytennisscoringpanel.viewmodel.ScoreViewModel
import com.pragathoys.easytennisscoringpanel.domain.GameMode
import com.pragathoys.easytennisscoringpanel.domain.Point
import com.pragathoys.easytennisscoringpanel.speech.SpeechManager
import com.pragathoys.easytennisscoringpanel.domain.buildAnnouncement
import com.pragathoys.easytennisscoringpanel.domain.matchAnnouncement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreboardScreen(vm: ScoreViewModel, speechManager: SpeechManager, onOpenSettings: () -> Unit) {
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state.speechEnabled) {
            val announcement = buildAnnouncement(state)
            val matchStatus = matchAnnouncement(state)
            if (announcement.isNotEmpty()) speechManager.speak(announcement)
            if (matchStatus.isNotEmpty()) speechManager.speak(matchStatus)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎾 Tennis Scoreboard", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Badge
            val statusText = when {
                state.matchOver -> "MATCH OVER"
                state.isSuperTiebreak -> "SUPER TIEBREAK"
                state.inTiebreak -> "TIEBREAK"
                state.isDoubles && state.aPoint == Point.FORTY && state.bPoint == Point.FORTY -> "DECIDING POINT"
                state.mode == GameMode.DEUCE -> "DEUCE"
                else -> "LIVE"
            }
            
            Surface(
                color = if (state.matchOver) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = statusText,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (state.matchOver) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            // Score Table
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ScoreHeaderRow()
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    
                    val aPointDisplay = if (state.inTiebreak) state.aTiebreakPoints.toString() else {
                        when (state.mode) {
                            GameMode.ADV_A -> "Ad"
                            GameMode.ADV_B -> "40"
                            else -> state.aPoint.displayValue
                        }
                    }
                    PlayerScoreRow(name = state.playerAName, sets = state.aSets, games = state.aGames, points = aPointDisplay)
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    
                    val bPointDisplay = if (state.inTiebreak) state.bTiebreakPoints.toString() else {
                        when (state.mode) {
                            GameMode.ADV_B -> "Ad"
                            GameMode.ADV_A -> "40"
                            else -> state.bPoint.displayValue
                        }
                    }
                    PlayerScoreRow(name = state.playerBName, sets = state.bSets, games = state.bGames, points = bPointDisplay)
                }
            }

            if (state.matchOver) {
                Text(
                    text = "Winner: ${state.winner}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1.0f))

            // Control Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { vm.pointA() },
                    modifier = Modifier.weight(1f).height(72.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.matchOver
                ) {
                    Text("Point\n${state.playerAName}", textAlign = TextAlign.Center)
                }
                Button(
                    onClick = { vm.pointB() },
                    modifier = Modifier.weight(1f).height(72.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.matchOver,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Point\n${state.playerBName}", textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { vm.undo() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("UNDO")
                }
                OutlinedButton(
                    onClick = { vm.reset() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("RESET")
                }
            }
        }
    }
}

@Composable
fun ScoreHeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("PLAYER", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
        Text("S", modifier = Modifier.width(40.dp), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelSmall)
        Text("G", modifier = Modifier.width(40.dp), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelSmall)
        Text("PTS", modifier = Modifier.width(60.dp), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun PlayerScoreRow(name: String, sets: Int, games: Int, points: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        
        ScoreBox(text = sets.toString(), color = MaterialTheme.colorScheme.surfaceVariant, width = 40.dp)
        Spacer(Modifier.width(4.dp))
        ScoreBox(text = games.toString(), color = MaterialTheme.colorScheme.surfaceVariant, width = 40.dp)
        Spacer(Modifier.width(8.dp))
        ScoreBox(
            text = points, 
            color = MaterialTheme.colorScheme.primaryContainer, 
            width = 60.dp, 
            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
        )
    }
}

@Composable
fun ScoreBox(
    text: String, 
    color: Color, 
    width: androidx.compose.ui.unit.Dp,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.width(width).height(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = textStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}
