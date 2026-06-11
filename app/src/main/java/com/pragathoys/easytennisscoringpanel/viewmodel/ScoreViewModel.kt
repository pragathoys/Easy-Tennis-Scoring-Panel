package com.pragathoys.easytennisscoringpanel.viewmodel

import androidx.lifecycle.ViewModel
import com.pragathoys.easytennisscoringpanel.domain.TennisEngine
import com.pragathoys.easytennisscoringpanel.domain.TennisState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull

class ScoreViewModel : ViewModel() {

    private val _state = MutableStateFlow<TennisState?>(null)
    // We expose a flow that filters out the initial null
    // But since collectAsState needs an initial value, we keep it nullable and handle in UI or use a dummy
    val stateFlow = _state.asStateFlow()

    fun setInitialState(state: TennisState) {
        if (_state.value == null) {
            _state.value = state
        }
    }

    private val history = mutableListOf<TennisState>()

    fun pointA() {
        _state.value?.let { current ->
            save(current)
            _state.value = TennisEngine.pointWon(current, true)
        }
    }

    fun pointB() {
        _state.value?.let { current ->
            save(current)
            _state.value = TennisEngine.pointWon(current, false)
        }
    }

    fun undo() {
        if (history.isNotEmpty()) {
            _state.value = history.removeLast()
        }
    }

    fun reset() {
        _state.value?.let { current ->
            save(current)
            // Reset to a fresh state but keep the configuration
            _state.value = TennisState(
                matchFormat = current.matchFormat,
                playerAName = current.playerAName,
                playerBName = current.playerBName,
                isDoubles = current.isDoubles,
                speechEnabled = current.speechEnabled
            )
        }
    }

    private fun save(current: TennisState) {
        history.add(current)
    }
}