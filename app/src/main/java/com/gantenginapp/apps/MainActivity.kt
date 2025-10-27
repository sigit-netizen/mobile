package com.gantenginapp.apps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.gantenginapp.apps.model.User
import com.gantenginapp.apps.navigation.AppNavHost
import com.gantenginapp.apps.onBoarding.LandingScreen
import com.gantenginapp.apps.ui.theme.LearnAndroidDasarTheme
import kotlinx.coroutines.delay
import com.gantenginapp.apps.networks.NetworkMonitor
import android.app.Application
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnAndroidDasarTheme {
                AppRoot()
            }
        }
    }
}


@Composable
fun AppRoot(app: Application = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application) {
    var currentUser by remember { mutableStateOf<User?>(null) }
    var currentScreen by remember { mutableStateOf("splash") }

    // Splash Delay 2 detik
    LaunchedEffect(Unit) {
        delay(2000L)
        currentScreen = "landing"
    }

    when (currentScreen) {
        "splash" -> SplashScreenContent()
        "landing" -> LandingScreen(
            onSkipClick = {
                // setelah skip, masuk ke sistem navigasi utama (login, register, home)
                currentScreen = "mainApp"
            }
        )

        "mainApp" -> {
            val navController = rememberNavController()
            AppNavHost(
                navController = navController,
                currentUser = currentUser,
                onUserLogin = { user ->
                    currentUser = user
                }
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth()
        ) {
            NetworkBanner(app)
        }
    }
}


@Composable
fun NetworkBanner(app: Application) {
    val monitor = remember { NetworkMonitor(app.applicationContext) }
    var isConnected by remember {mutableStateOf(true)}

    LaunchedEffect(Unit) {
        monitor.isConnected.collectLatest {
            isConnected = it
        }
    }
    if (!isConnected) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFD32F2F))
                .padding(8.dp),

            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "️⚠️ Tidak ada koneksi internet",
                color = Color.White
            )
        }
    }
}
