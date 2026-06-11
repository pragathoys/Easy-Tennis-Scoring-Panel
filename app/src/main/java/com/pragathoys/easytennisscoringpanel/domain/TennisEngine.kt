package com.pragathoys.easytennisscoringpanel.domain

object TennisEngine {

    fun pointWon(state: TennisState, playerA: Boolean): TennisState {

        if (state.matchOver) {
            return state
        }


        return when (state.mode) {
            GameMode.NEW_GAME -> handleNormal(state, playerA)

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

        return state.copy(aPoint = a, bPoint = b, mode = GameMode.NORMAL)
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
//        return if (playerA) {
//            state.copy(
//                aGames = state.aGames + 1,
//                aPoint = Point.ZERO,
//                bPoint = Point.ZERO,
//                mode = GameMode.NEW_GAME
//            )
//        } else {
//            state.copy(
//                bGames = state.bGames + 1,
//                aPoint = Point.ZERO,
//                bPoint = Point.ZERO,
//                mode = GameMode.NEW_GAME
//            )
//        }

        val newA = if (playerA) state.aGames + 1 else state.aGames
        val newB = if (!playerA) state.bGames + 1 else state.bGames

        var newState = state.copy(
            aGames = newA,
            bGames = newB,
            aPoint = Point.ZERO,
            bPoint = Point.ZERO,
            mode = GameMode.NORMAL
        )

        if (newA == 6 && newB == 6) {
            return newState.copy(
                inTiebreak = true,
                aPoint = Point.ZERO,
                bPoint = Point.ZERO
            )
        }

        if (isSetWon(newA, newB)) {
            return winSet(newState, playerA)
        }

        return newState
    }

    private fun next(p: Point): Point {
        return when (p) {
            Point.ZERO -> Point.FIFTEEN
            Point.FIFTEEN -> Point.THIRTY
            Point.THIRTY -> Point.FORTY
            Point.FORTY -> Point.ADV
            Point.ADV -> Point.ADV
        }
    }

    private fun isGameWon(a: Point, b: Point): Boolean {
        val aVal = toInt(a)
        val bVal = toInt(b)

        return (aVal >= 4 || bVal >= 4) &&
                kotlin.math.abs(aVal - bVal) >= 2
    }

    private fun isTiebreakWon(a: Int, b: Int): Boolean {
        return (a >= 7 || b >= 7) &&
                kotlin.math.abs(a - b) >= 2
    }

    private fun isSetWon(aGames: Int, bGames: Int): Boolean {

        if (aGames >= 6 && aGames - bGames >= 2)
            return true

        if (bGames >= 6 && bGames - aGames >= 2)
            return true

        if (aGames == 7 && bGames == 6)
            return true

        if (bGames == 7 && aGames == 6)
            return true

        return false
    }

    private fun isMatchWon(aSets: Int, bSets: Int, bestOf: Int): Boolean {
        val setsToWin = (bestOf / 2) + 1
        return aSets >= setsToWin || bSets >= setsToWin
    }

    private fun winTiebreak(
        state: TennisState,
        playerA: Boolean
    ): TennisState {

        val aSets =
            if (playerA) state.aSets + 1
            else state.aSets

        val bSets =
            if (!playerA) state.bSets + 1
            else state.bSets

        return state.copy(
            aPoint = Point.ZERO,
            bPoint = Point.ZERO,
            aGames = 0,
            bGames = 0,
            aSets = aSets,
            bSets = bSets,
            inTiebreak = false
        )
    }

    private fun winSet(state: TennisState, playerA: Boolean): TennisState {

        val newAsets = if (playerA) state.aSets + 1 else state.aSets
        val newBsets = if (!playerA) state.bSets + 1 else state.bSets

        val matchWon = isMatchWon(newAsets, newBsets, state.bestOfSets)

        if (matchWon) {
            return state.copy(
                aSets = newAsets,
                bSets = newBsets,

                aGames = 0,
                bGames = 0,
                aPoint = Point.ZERO,
                bPoint = Point.ZERO,

                inTiebreak = false,
                matchOver = true,

                winner = if (playerA) state.playerAName else state.playerBName
            )
        }

        return state.copy(
            aSets = newAsets,
            bSets = newBsets,

            aGames = 0,
            bGames = 0,
            aPoint = Point.ZERO,
            bPoint = Point.ZERO,

            inTiebreak = false
        )
    }
    private fun toInt(p: Point): Int {
//        Log.d("TennisEngine", "toInt")
        return when (p) {
            Point.ZERO -> 0
            Point.FIFTEEN -> 1
            Point.THIRTY -> 2
            Point.FORTY -> 3
            Point.ADV -> 4
        }
    }

    private fun isImmediateGameWin(a: Point, b: Point): Boolean {
        val aVal = toInt(a)
        val bVal = toInt(b)

        return (aVal == 4 && bVal <= 2) ||
                (bVal == 4 && aVal <= 2)
    }

}