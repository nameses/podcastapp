package com.podcastapp.ui.navigation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private var timerJob: Job? = null

    fun startTimer(duration: Long, onFinish: () -> Unit) {
        stopTimer()
        _isTimerRunning.value = true

        timerJob = viewModelScope.launch {
            delay(duration)
            _isTimerRunning.value = false
            onFinish()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _isTimerRunning.value = false
    }
}
