package com.pragathoys.easytennisscoringpanel.domain

enum class Point {
    ZERO, FIFTEEN, THIRTY, FORTY, ADV
}

enum class GameMode {
    NEW_GAME,
    NORMAL,
    DEUCE,
    ADV_A,
    ADV_B
}

data class TennisState(
    val aPoint: Point = Point.ZERO,
    val bPoint: Point = Point.ZERO,
    val aGames: Int = 0,
    val bGames: Int = 0,
    val mode: GameMode = GameMode.NORMAL
)