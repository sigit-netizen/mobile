package com.gantenginapp.apps.ui.screen.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gantenginapp.apps.ui.screen.login.LoginActivity

class LandingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Langsung tampilkan LandingScreen, tanpa delay
            LandingScreen(
                onSkipClick = {
                    startActivity(Intent(this@LandingActivity, LoginActivity::class.java))
                    finish()
                }
            )
        }
    }
}