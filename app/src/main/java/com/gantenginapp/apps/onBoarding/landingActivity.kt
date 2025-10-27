package com.gantenginapp.apps.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gantenginapp.apps.ui.theme.LearnAndroidDasarTheme
import com.gantenginapp.apps.MainActivity // Pastikan path-nya sesuai
import kotlin.jvm.java

class LandingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnAndroidDasarTheme {
                LandingScreen(
                    onSkipClick = {
                        // Pindah ke halaman login
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}
