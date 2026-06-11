package com.pragathoys.easytennisscoringpanel.domain

import android.util.Log

object TennisEngine {

    fun pointWon(state: TennisState, playerA: Boolean): TennisState {

        return when (state.mode) {
            GameMode.NEW_GAME -> handleNormal(state, playerA)

            GameMode.NORMAL -> handleNormal(state, playerA)

            GameMode.DEUCE -> handleDeuce(state, playerA)

            GameMode.ADV_A -> handleAdvA(state, playerA)

            GameMode.ADV_B -> handleAdvB(state, playerA)
        }
    }

    private fun handleNormal(state: TennisState, playerA: Boolean): TennisState {

        Log.d("TennisEngine", "handleNormal")

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
        Log.d("TennisEngine", "handleDeuce")
        return if (playerA) {
            state.copy(mode = GameMode.ADV_A)
        } else {
            state.copy(mode = GameMode.ADV_B)
        }
    }

    private fun handleAdvA(state: TennisState, playerA: Boolean): TennisState {
        Log.d("TennisEngine", "handleAdvA")
        return if (playerA) {
            winGame(state, true)
        } else {
            state.copy(mode = GameMode.DEUCE)
        }
    }

    private fun handleAdvB(state: TennisState, playerA: Boolean): TennisState {
        Log.d("TennisEngine", "handleAdvB")
        return if (playerA) {
            state.copy(mode = GameMode.DEUCE)
        } else {
            winGame(state, false)
        }
    }

    private fun winGame(state: TennisState, playerA: Boolean): TennisState {
        Log.d("TennisEngine", "winGame")
        return if (playerA) {
            state.copy(
                aGames = state.aGames + 1,
                aPoint = Point.ZERO,
                bPoint = Point.ZERO,
                mode = GameMode.NEW_GAME
            )
        } else {
            state.copy(
                bGames = state.bGames + 1,
                aPoint = Point.ZERO,
                bPoint = Point.ZERO,
                mode = GameMode.NEW_GAME
            )
        }
    }

    private fun next(p: Point): Point {
        Log.d("TennisEngine", "next")
        return when (p) {
            Point.ZERO -> Point.FIFTEEN
            Point.FIFTEEN -> Point.THIRTY
            Point.THIRTY -> Point.FORTY
            Point.FORTY -> Point.ADV
            Point.ADV -> Point.ADV
        }
    }

    private fun isGameWon(a: Point, b: Point): Boolean {
        Log.d("TennisEngine", "isGameWon")
        val aVal = toInt(a)
        val bVal = toInt(b)

        return (aVal >= 4 || bVal >= 4) &&
                kotlin.math.abs(aVal - bVal) >= 2
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
        Log.d("TennisEngine", "isImmediateGameWin")
        val aVal = toInt(a)
        val bVal = toInt(b)
        Log.d("TennisEngine", "aVal=" + aVal)
        Log.d("TennisEngine", "bVal=" + bVal)

        return (aVal == 4 && bVal <= 2) ||
                (bVal == 4 && aVal <= 2)
    }

}