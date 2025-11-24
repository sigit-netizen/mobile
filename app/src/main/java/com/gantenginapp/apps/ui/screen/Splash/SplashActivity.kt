package com.gantenginapp.apps.ui.screen.Splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.*
import com.gantenginapp.apps.ui.screen.onBoarding.LandingActivity
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MySplashScreen {
                startActivity(Intent(this@SplashActivity, LandingActivity::class.java))
                finish()
            }
        }
    }
}

@Composable
fun MySplashScreen(onTimeout : () -> Unit) {
    // Delay sebelum lanjut ke MainActivity
    LaunchedEffect(Unit) {
        delay(2000L)
        onTimeout()
    }
    SplashScreenUI()
}
