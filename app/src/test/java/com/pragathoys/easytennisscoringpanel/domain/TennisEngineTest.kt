package com.pragathoys.easytennisscoringpanel.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class TennisEngineTest {

    @Test
    fun `initial state is zero-zero`() {
        val state = TennisState()
        assertEquals(Point.ZERO, state.aPoint)
        assertEquals(Point.ZERO, state.bPoint)
        assertEquals(0, state.aGames)
        assertEquals(0, state.bGames)
    }

    @Test
    fun `point for A moves from ZERO to FIFTEEN`() {
        var state = TennisState()
        state = TennisEngine.pointWon(state, true)
        assertEquals(Point.FIFTEEN, state.aPoint)
    }

    @Test
    fun `point sequence ZERO FIFTEEN THIRTY FORTY`() {
        var state = TennisState()
        
        state = TennisEngine.pointWon(state, true) // 15
        assertEquals(Point.FIFTEEN, state.aPoint)
        
        state = TennisEngine.pointWon(state, true) // 30
        assertEquals(Point.THIRTY, state.aPoint)
        
        state = TennisEngine.pointWon(state, true) // 40
        assertEquals(Point.FORTY, state.aPoint)
    }

    @Test
    fun `forty-forty enters DEUCE mode`() {
        var state = TennisState()
        
        // Player A to 40
        state = TennisEngine.pointWon(state, true)
        state = TennisEngine.pointWon(state, true)
        state = TennisEngine.pointWon(state, true)
        
        // Player B to 40
        state = TennisEngine.pointWon(state, false)
        state = TennisEngine.pointWon(state, false)
        state = TennisEngine.pointWon(state, false)
        
        assertEquals(GameMode.DEUCE, state.mode)
    }

    @Test
    fun `deuce to advantage A`() {
        var state = TennisState(mode = GameMode.DEUCE)
        
        state = TennisEngine.pointWon(state, true)
        assertEquals(GameMode.ADV_A, state.mode)
    }

    @Test
    fun `advantage A back to deuce if B wins point`() {
        var state = TennisState(mode = GameMode.ADV_A)
        
        state = TennisEngine.pointWon(state, false)
        assertEquals(GameMode.DEUCE, state.mode)
    }

    @Test
    fun `win game from forty-thirty`() {
        var state = TennisState(aPoint = Point.FORTY, bPoint = Point.THIRTY)
        
        state = TennisEngine.pointWon(state, true)
        
        assertEquals(1, state.aGames)
        assertEquals(Point.ZERO, state.aPoint)
        assertEquals(Point.ZERO, state.bPoint)
    }

    @Test
    fun `win game from advantage A`() {
        var state = TennisState(mode = GameMode.ADV_A)
        
        state = TennisEngine.pointWon(state, true)
        
        assertEquals(1, state.aGames)
        assertEquals(GameMode.NORMAL, state.mode)
    }

    @Test
    fun `win set at 6-0`() {
        var state = TennisState(aGames = 5, aPoint = Point.FORTY)
        state = TennisEngine.pointWon(state, true)
        
        assertEquals(1, state.aSets)
        assertEquals(0, state.aGames)
    }

    @Test
    fun `enter tiebreak at 6-6`() {
        // Player A wins 5 games, Player B wins 6 games
        var state = TennisState(aGames = 5, bGames = 6, aPoint = Point.FORTY)
        // A wins game to make it 6-6
        state = TennisEngine.pointWon(state, true)
        
        assertEquals(6, state.aGames)
        assertEquals(6, state.bGames)
        assertEquals(true, state.inTiebreak)
    }

    @Test
    fun `win match best of 3`() {
        var state = TennisState(aSets = 1, aGames = 5, aPoint = Point.FORTY, bestOfSets = 3)
        state = TennisEngine.pointWon(state, true)
        
        assertEquals(2, state.aSets)
        assertEquals(true, state.matchOver)
        assertEquals("PLAYER A", state.winner)
    }

    @Test
    fun `not win match best of 5 at 2 sets`() {
        var state = TennisState(aSets = 1, aGames = 5, aPoint = Point.FORTY, bestOfSets = 5)
        state = TennisEngine.pointWon(state, true)
        
        assertEquals(2, state.aSets)
        assertEquals(false, state.matchOver)
    }

    @Test
    fun `win match best of 5 at 3 sets`() {
        var state = TennisState(aSets = 2, aGames = 5, aPoint = Point.FORTY, bestOfSets = 5)
        state = TennisEngine.pointWon(state, true)
        
        assertEquals(3, state.aSets)
        assertEquals(true, state.matchOver)
        assertEquals("PLAYER A", state.winner)
    }
}
