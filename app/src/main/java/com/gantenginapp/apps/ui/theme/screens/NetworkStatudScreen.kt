package com.gantenginapp.apps.ui.theme.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gantenginapp.apps.networks.NetworkMonitor
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NetworkStatusScreen(app: Application) {
    val monitor = remember { NetworkMonitor(app.applicationContext) }

    var isConnected by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        monitor.isConnected.collectLatest {
            isConnected = it
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isConnected) {
            Text("✅ Terkoneksi ke Internet", color = MaterialTheme.colorScheme.primary)
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🚫 Tidak ada koneksi Internet", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* bisa tambahkan retry action di sini */ }) {
                    Text("Coba Lagi")
                }
            }
        }
    }
}
