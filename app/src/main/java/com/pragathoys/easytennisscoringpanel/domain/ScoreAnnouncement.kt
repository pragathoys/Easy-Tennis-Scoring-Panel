package com.pragathoys.easytennisscoringpanel.domain

fun Point.toDisplayName(): String {
    return when (this) {
        Point.ZERO -> "Love"
        Point.FIFTEEN -> "Fifteen"
        Point.THIRTY -> "Thirty"
        Point.FORTY -> "Forty"
    }
}

fun buildAnnouncement(state: TennisState): String {

    return when (state.mode) {

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