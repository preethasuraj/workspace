package com.example.cart1.timer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow<List<Timer>>(emptyList())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = getTimers(4)
    }




    fun startTimer(id: Long) {

        val job = viewModelScope.launch {
            while(true) {
            delay(1000)
            _uiState.update { timers ->
                timers.map { timer ->
                    if (timer.id == id) {
                        timer.copy(duration = timer.duration + 1)
                    } else {
                        timer
                    }
                }
            }
                }

        }
        _uiState.update { timers ->
            timers.map {
                if (it.id == id) {
                    it.copy(
                        job = job,
                    )
                } else {
                    it
                }
            }
        }
    }


    fun stopTimer(id: Long) {

        viewModelScope.launch {
            _uiState.update { timers ->
                timers.map { timer ->
                    if (timer.id == id) {
                        timer.job?.cancel()
                        timer.copy(job = null)
                    } else {
                        timer
                    }
                }
            }


        }
    }
}

data class Timer(
    val id: Long,
    val duration: Long,
    val job: Job?
)

fun getTimers(count: Int): List<Timer> {
    val result = mutableListOf<Timer>()
    for (i in 0..count) {
        result.add(
            Timer(
                id = i.toLong(),
                duration = 0L,
                job = null

            )
        )
    }
    return result
}

