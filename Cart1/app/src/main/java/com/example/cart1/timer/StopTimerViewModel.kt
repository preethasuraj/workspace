package com.example.cart1.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StopTimerViewModel(
    val savedStateHandle: SavedStateHandle
)
    : ViewModel(){
    private val _uiState = MutableStateFlow(StopTimer())
    val uiState = _uiState.asStateFlow()

    var job: Job? = null

    init {
        val endTime = savedStateHandle.get<Long>(keys)
        endTime?.toLong()?.let {
            if(it > System.currentTimeMillis()) {
                startTimer(endTime)
            }
        }
    }

    fun startTimer(end: Long? = null) {
        val endTime = end ?: (System.currentTimeMillis() + WAIT_TIME)
        savedStateHandle.set(keys, endTime)
        while(true){
            val remainingTime = endTime - System.currentTimeMillis()
            if(remainingTime != 0L){
                job = viewModelScope.launch {
                    delay(1000)
                }
                _uiState.update { state ->
                    state.copy(
                        duration = state.duration + 1
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        duration = 0
                    )
                }
                if(job?.isActive == true){
                    job?.cancel()
                }
            }
        }


    }
    companion object {
        const val WAIT_TIME = 30000L
        const val keys = "stop-time"
    }
}

data class StopTimer(
    val duration: Long = 0,

)