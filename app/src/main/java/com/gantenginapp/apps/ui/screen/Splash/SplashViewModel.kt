package com.gantenginapp.apps.ui.screen.Splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _navigateToNext = MutableStateFlow(false)
    val navigateToNext: StateFlow<Boolean> = _navigateToNext

    init {
        // Mulai delay saat ViewModel dibuat
        startSplashDelay()
    }

    private fun startSplashDelay() {
        viewModelScope.launch {
            delay(2000L) // 2 detik
            _navigateToNext.value = true
        }
    }
}