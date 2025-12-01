package com.gantenginapp.apps.ui.screen.adminstore



import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.gantenginapp.apps.ui.screen.StoreBarber.BarberStoreViewModel
import com.gantenginapp.apps.ui.screen.home.HomeActivity
import com.gantenginapp.apps.data.repository.StoreRepository
import com.gantenginapp.apps.data.repository.UserRepository
import com.gantenginapp.apps.data.local.UserPreferences
import com.gantenginapp.apps.data.remote.RetrofitClient

class AdminStoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = UserPreferences(this)

        val storeRepository = StoreRepository(RetrofitClient.instance)
        val userRepository = UserRepository(prefs)
        val factory = AdminStoreViewModelFactory(storeRepository,userRepository)

        val viewModel = ViewModelProvider(this, factory)
            .get(AdminStoreViewModel::class.java)

        setContent {
            AdminStoreScreen (
                viewModel,
                onBackClick = {
                    val intent = Intent(this@AdminStoreActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}

