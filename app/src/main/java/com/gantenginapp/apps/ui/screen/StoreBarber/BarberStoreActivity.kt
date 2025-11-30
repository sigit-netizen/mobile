package com.gantenginapp.apps.ui.screen.StoreBarber

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import com.gantenginapp.apps.ui.screen.StoreBarber.BarberDetailScreen
import com.gantenginapp.apps.ui.screen.home.HomeActivity
import androidx.lifecycle.ViewModelProvider
import com.gantenginapp.apps.data.repository.*
import com.gantenginapp.apps.data.remote.RetrofitClient
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.gantenginapp.apps.data.local.UserPreferences
class BarberStoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = intent.getIntExtra("STORE_ID", 0)
        val prefs = UserPreferences(this)

        // Buat repository (sementara manual)
        val storeRepository = StoreRepository(RetrofitClient.instance)
        val userRepository = UserRepository(prefs)

        // Buat factory
        val factory = BarberStoreViewModelFactory(storeId, storeRepository, userRepository)

        // Ambil viewmodel lewat factory
        val viewModel = ViewModelProvider(this, factory)
            .get(BarberStoreViewModel::class.java)

        setContent {
            BarberDetailScreen(
                viewModel = viewModel,
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
