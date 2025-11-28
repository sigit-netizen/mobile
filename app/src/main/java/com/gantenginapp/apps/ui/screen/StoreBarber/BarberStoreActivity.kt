package com.gantenginapp.apps.ui.screen.StoreBarber

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gantenginapp.apps.ui.screen.StoreBarber.BarberDetailScreen
import com.gantenginapp.apps.ui.screen.home.HomeActivity

class BarberStoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarberDetailScreen(
                onBackClick = {
                    val intent = Intent(this@BarberStoreActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}