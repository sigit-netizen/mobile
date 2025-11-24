package com.gantenginapp.apps


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.gantenginapp.apps.ui.screen.Splash.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Langsung buka IntroActivity
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish() // Tutup MainActivity supaya tidak bisa kembali ke sini
    }
}

