package com.gantenginapp.apps

// Color
import  com.gantenginapp.apps.ui.theme.ColorCustom

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gantenginapp.apps.ui.theme.LearnAndroidDasarTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.gantenginapp.apps.R
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.*


class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnAndroidDasarTheme {
                MySplashScreen {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun MySplashScreen(onTimeout : () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000L)
        onTimeout()
    }
    SplashScreenContent()
}



// 🔹 Ini untuk UI murni (bisa di-preview)
@Composable
fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.gantengin),
                contentDescription = "App logo",
                modifier = Modifier.size(260.dp)
            )
            CircularProgressIndicator(
                color = ColorCustom.dark
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    LearnAndroidDasarTheme {
        SplashScreenContent()
    }
}


