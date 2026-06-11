package com.pragathoys.easytennisscoringpanel.domain

enum class Point(val displayValue: String) {
    ZERO("0"), 
    FIFTEEN("15"), 
    THIRTY("30"), 
    FORTY("40"), 
    ADV("Ad")
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

enum class MatchFormat {
    BEST_OF_3,
    BEST_OF_5,
    TWO_SETS_AND_SUPER_TIEBREAK
}

data class TennisState(
    // Current Game
    val aPoint: Point = Point.ZERO,
    val bPoint: Point = Point.ZERO,
    // Current Tiebreak Points
    val aTiebreakPoints: Int = 0,
    val bTiebreakPoints: Int = 0,
    // Current Set
    val aGames: Int = 0,
    val bGames: Int = 0,

    // Match
    val aSets: Int = 0,
    val bSets: Int = 0,

    // Tiebreak
    val inTiebreak: Boolean = false,
    val isSuperTiebreak: Boolean = false,
    // Match is over
    val matchOver: Boolean = false,

    // Winner
    val winner: String? = null,

    // Match config
    val matchFormat: MatchFormat = MatchFormat.TWO_SETS_AND_SUPER_TIEBREAK,
    val bestOfSets: Int = 3,
    val playerAName: String = "Player A",
    val playerBName: String = "Player B",
    val isDoubles: Boolean = false,
    val speechEnabled: Boolean = true,

    val mode: GameMode = GameMode.NORMAL
)