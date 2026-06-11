package com.pragathoys.easytennisscoringpanel.domain

fun Point.toDisplayName(): String {
    return when (this) {
        Point.ZERO -> "Love"
        Point.FIFTEEN -> "Fifteen"
        Point.THIRTY -> "Thirty"
        Point.FORTY -> "Forty"
        Point.ADV -> "Advantage"
    }
}

fun buildAnnouncement(state: TennisState): String {
    if (state.matchOver) return ""

    if (state.aPoint == Point.ZERO && state.bPoint == Point.ZERO && !state.inTiebreak) {
        if (state.aGames == 0 && state.bGames == 0) {
            return "New game"
        }
        
        // Game just finished
        val gameScore = if (state.aGames > state.bGames) {
            "${state.playerAName} leads ${state.aGames} games to ${state.bGames}"
        } else if (state.bGames > state.aGames) {
            "${state.playerBName} leads ${state.bGames} games to ${state.aGames}"
        } else {
            "${state.aGames} games all"
        }
        return "Game. $gameScore"
    }

    return when (state.mode) {
        GameMode.NEW_GAME -> "New game"

        GameMode.DEUCE -> "Deuce"

        GameMode.ADV_A -> "Advantage " + state.playerAName

        GameMode.ADV_B -> "Advantage " + state.playerBName

        else -> {
            if (state.aPoint == state.bPoint) {
                "${state.aPoint.toDisplayName()} all"
            } else {
                "${state.aPoint.toDisplayName()} ${state.bPoint.toDisplayName()}"
            }
        }
    }
}

fun matchAnnouncement(state: TennisState): String {

    if (state.matchOver) {
        return "Game, Set and Match, ${state.winner}. Score: ${state.aSets} sets to ${state.bSets}"
    }

    if (state.aGames == 0 && state.bGames == 0 && (state.aSets > 0 || state.bSets > 0)) {
        // Set just finished
        return "Game and set. ${state.aSets} sets to ${state.bSets}"
    }

    if (state.inTiebreak) {
        return "Tiebreak"
    }

    return ""
}
