package com.pragathoys.easytennisscoringpanel.domain
object TennisEngine {

    fun pointWon(state: TennisState, playerA: Boolean): TennisState {

        return when (state.mode) {

            GameMode.NORMAL -> handleNormal(state, playerA)

            GameMode.DEUCE -> handleDeuce(state, playerA)

            GameMode.ADV_A -> handleAdvA(state, playerA)

            GameMode.ADV_B -> handleAdvB(state, playerA)
        }
    }

    private fun handleNormal(state: TennisState, playerA: Boolean): TennisState {

        var a = state.aPoint
        var b = state.bPoint

        if (playerA) a = next(a) else b = next(b)

        // 2. CHECK IMMEDIATE GAME WIN (IMPORTANT FIX)
        if (isImmediateGameWin(a, b)) {
            return winGame(state.copy(aPoint = a, bPoint = b), playerA)
        }

        // DEUCE CHECK
        if (a == Point.FORTY && b == Point.FORTY) {
            return state.copy(aPoint = a, bPoint = b, mode = GameMode.DEUCE)
        }

        // GAME WIN CHECK (ONLY HERE)
        if (isGameWon(a, b)) {
            return winGame(state.copy(aPoint = a, bPoint = b), playerA)
        }

        return state.copy(aPoint = a, bPoint = b)
    }

    private fun handleDeuce(state: TennisState, playerA: Boolean): TennisState {
        return if (playerA) {
            state.copy(mode = GameMode.ADV_A)
        } else {
            state.copy(mode = GameMode.ADV_B)
        }
    }

    private fun handleAdvA(state: TennisState, playerA: Boolean): TennisState {

        return if (playerA) {
            winGame(state, true)
        } else {
            state.copy(mode = GameMode.DEUCE)
        }
    }

    private fun handleAdvB(state: TennisState, playerA: Boolean): TennisState {

        return if (playerA) {
            state.copy(mode = GameMode.DEUCE)
        } else {
            winGame(state, false)
        }
    }

    private fun winGame(state: TennisState, playerA: Boolean): TennisState {

        return if (playerA) {
            state.copy(
                aGames = state.aGames + 1,
                aPoint = Point.ZERO,
                bPoint = Point.ZERO,
                mode = GameMode.NORMAL
            )
        } else {
            state.copy(
                bGames = state.bGames + 1,
                aPoint = Point.ZERO,
                bPoint = Point.ZERO,
                mode = GameMode.NORMAL
            )
        }
    }

    private fun next(p: Point): Point {
        return when (p) {
            Point.ZERO -> Point.FIFTEEN
            Point.FIFTEEN -> Point.THIRTY
            Point.THIRTY -> Point.FORTY
            Point.FORTY -> Point.FORTY
        }
    }

    private fun isGameWon(a: Point, b: Point): Boolean {
        val aVal = toInt(a)
        val bVal = toInt(b)

        return (aVal >= 4 || bVal >= 4) &&
                kotlin.math.abs(aVal - bVal) >= 2
    }

    private fun toInt(p: Point): Int {
        return when (p) {
            Point.ZERO -> 0
            Point.FIFTEEN -> 1
            Point.THIRTY -> 2
            Point.FORTY -> 3
        }
    }

    private fun isImmediateGameWin(a: Point, b: Point): Boolean {

        val aVal = toInt(a)
        val bVal = toInt(b)

        return (aVal == 4 && bVal <= 2) ||
                (bVal == 4 && aVal <= 2)
    }

}