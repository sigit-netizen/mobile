// File: SplashScreen.kt
package com.gantenginapp.apps.ui.screen.Splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gantenginapp.apps.R
import com.gantenginapp.apps.ui.theme.ColorCustom
import com.gantenginapp.apps.ui.theme.LearnAndroidDasarTheme
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SplashScreenContent(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000L)
        onTimeout()
    }

    SplashScreenUI()
}

@Composable
fun SplashScreenUI() {
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

            Spacer(modifier = Modifier.height(20.dp))

            CircularProgressIndicator(
                color = ColorCustom.dark
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    LearnAndroidDasarTheme {
        SplashScreenUI()
    }
}