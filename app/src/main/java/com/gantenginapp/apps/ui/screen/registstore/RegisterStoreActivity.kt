package com.gantenginapp.apps.ui.screen.registstore

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import com.gantenginapp.apps.data.local.UserPreferences
import com.gantenginapp.apps.data.remote.RetrofitClient
import com.gantenginapp.apps.data.repository.StoreRepository
import com.gantenginapp.apps.data.repository.UserRepository
import com.gantenginapp.apps.ui.screen.home.HomeActivity
import androidx.compose.runtime.*

class RegistStoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val prefs = UserPreferences(this)
        val api = RetrofitClient.instance

        val userRepository = UserRepository(prefs)
        val storeRepository = StoreRepository(api)

        setContent {


            val viewModel: RegisterStoreViewModel = viewModel(
                factory = RegistStoreViewModelFactory(storeRepository, userRepository)
            )


            val userId = viewModel.userId.collectAsState().value

            RegisterStoreScreen(
                onBackClick = {
                    val intent = Intent(this@RegistStoreActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()
                },
                viewModel = viewModel,

                onRegisterSuccess = {

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}
