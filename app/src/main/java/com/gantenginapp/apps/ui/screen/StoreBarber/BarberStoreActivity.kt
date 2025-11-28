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
import com.gantenginapp.apps.data.repository.StoreRepository
import com.gantenginapp.apps.data.remote.RetrofitClient
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp

class BarberStoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storeId = intent.getIntExtra("STORE_ID", 0)

        // Buat repository (sementara manual)
        val storeRepository = StoreRepository(RetrofitClient.instance)

        // Buat factory
        val factory = BarberStoreViewModelFactory(storeId, storeRepository)

        // Ambil viewmodel lewat factory
        val viewModel = ViewModelProvider(this, factory)
            .get(BarberStoreViewModel::class.java)

        setContent {
            val isLoading by viewModel.isRefreshingLoading.collectAsState()
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
            SwipeRefresh(state = swipeRefreshState,
                onRefresh = {
                    viewModel.loadDataStore()
                },
                indicatorPadding = PaddingValues(top = 28.dp)
                ) {
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
}
