package com.example.vrbointerview.timer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState())

    val uiStateTimer = _uiState.asStateFlow()

    fun onStart(id: Int) {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val seconds = _uiState.value.timerState
                _uiState.update { state ->
                    state.copy(
                        timerState = seconds.mapIndexed { index, lng ->
                            if (index == id) {
                                lng + 1
                            } else {
                                lng
                            }
                        }
                    )
                }
            }

        }
    }
}


data class UiState(
    val timerState: List<TimerState> = mutableListOf()
)

data class TimerState(
    val seconds: Long,
    val paused: Boolean
)