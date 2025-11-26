package com.gantenginapp.apps.ui.screen.Splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.ui.screen.onBoarding.LandingActivity

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val shouldNavigate = remember { mutableStateOf(false) }

            // Gunakan ViewModel
            val splashViewModel: SplashViewModel = viewModel()

            if (!shouldNavigate.value) {
                SplashScreen(
                    viewModel = splashViewModel,
                    onNavigateToNext = {
                        shouldNavigate.value = true
                        startActivity(Intent(this@SplashActivity, LandingActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}