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

enum class MatchMode {
    NORMAL,
    DEUCE,
    ADV_A,
    ADV_B,
    TIEBREAK
}

data class TennisState(
    // Current Game
    val aPoint: Point = Point.ZERO,
    val bPoint: Point = Point.ZERO,
    // Current Set
    val aGames: Int = 0,
    val bGames: Int = 0,

    // Match
    val aSets: Int = 0,
    val bSets: Int = 0,

    // Tiebreak
    val inTiebreak: Boolean = false,
    // Match is over
    val matchOver: Boolean = false,

    // Winner
    val winner: String? = null,

    // Match config
    val bestOfSets: Int = 3,
    val playerAName: String = "Player A",
    val playerBName: String = "Player B",
    val isDoubles: Boolean = false,
    val speechEnabled: Boolean = true,

    val mode: GameMode = GameMode.NORMAL
)