package com.pragathoys.easytennisscoringpanel.viewmodel

import androidx.lifecycle.ViewModel
import com.pragathoys.easytennisscoringpanel.domain.TennisEngine
import com.pragathoys.easytennisscoringpanel.domain.TennisState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScoreViewModel : ViewModel() {

    private val _state = MutableStateFlow(TennisState())
    val state: StateFlow<TennisState> = _state

    fun setInitialState(state: TennisState) {
        _state.value = state
    }

    private val history = mutableListOf<TennisState>()

    fun pointA() {
        save()
        _state.value = TennisEngine.pointWon(_state.value, true)
    }

    fun pointB() {
        save()
        _state.value = TennisEngine.pointWon(_state.value, false)
    }

    fun undo() {
        if (history.isNotEmpty()) {
            _state.value = history.removeLast()
        }
    }

    fun reset() {
        save()
        _state.value = TennisState()
    }

    private fun save() {
        history.add(_state.value)
    }
}