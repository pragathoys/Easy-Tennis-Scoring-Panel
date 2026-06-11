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

    return when (state.mode) {
        GameMode.NEW_GAME ->
            "New game!"

        GameMode.DEUCE ->
            "Deuce"

        GameMode.ADV_A ->
            "Advantage Player A"

        GameMode.ADV_B ->
            "Advantage Player B"

        else ->
            "${state.aPoint.toDisplayName()} ${state.bPoint.toDisplayName()}"
    }
}