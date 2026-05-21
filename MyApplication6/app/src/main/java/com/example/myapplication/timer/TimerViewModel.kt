package com.example.myapplication.timer

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(
        UiState(emptyList())
    )
    val uiState = _uiState.asStateFlow()

    val _toast = MutableStateFlow<Int?>(null)
    val toast = _toast.asStateFlow()

    var identifier = 0

    fun onIntent(intent: Intent) {
        when (intent) {
            Intent.Add -> {
                _toast.value = identifier
                _uiState.update { state ->
                    state.copy(
                        list = state.list + Timer(
                            id = identifier++
                        )
                    )
                }
                identifier
            }

            is Intent.Pause -> {

                _uiState.update { state ->
                    state.copy(
                        list = state.list.map { timer ->
                            if(timer.id == intent.id){
                                timer.jobId?.cancel()
                                timer.copy(jobId = null)
                            } else timer
                        }
                    )
                }
            }
            is Intent.Start -> {
                val jobId = viewModelScope.launch {
                    val timer = _uiState.value.list.first{it.id == intent.id}
                    val start = System.currentTimeMillis()
                    val base = timer.duration
                    while(true) {
                        delay(200)
                        val elapsed = (System.currentTimeMillis() - start)/1000
                        if(elapsed > (base - start)){
                            _uiState.update { state ->
                                state.copy(
                                    list = state.list.map { timer ->
                                        if(timer.id == intent.id){
                                            timer.copy(duration = base + elapsed)
                                        } else timer
                                    }
                                )
                            }
                        }
                    }
                }
                _uiState.update { state ->
                    state.copy(
                        list = state.list.map { timer ->
                            if(timer.id == intent.id){
                                timer.copy(jobId = jobId)
                            } else timer
                        }
                    )
                }
            }
        }
    }
}

data class Timer(
    val id: Int,
    val duration: Long = 0L,
    val jobId: Job? = null
)

data class UiState(
    val list: List<Timer>
)

sealed class Intent {
    data object Add : Intent()
    data class Start(val id: Int) : Intent()
    data class Pause(val id: Int) : Intent()
}