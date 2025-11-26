// app/src/main/java/com/gantenginapp/apps/ui/screen/profil/ProfileActivity.kt
package com.gantenginapp.apps.ui.screen.profil

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.gantenginapp.apps.R
import com.gantenginapp.apps.data.remote.RetrofitClient
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import com.gantenginapp.apps.ui.screen.home.HomeActivity
import com.gantenginapp.apps.ui.screen.login.LoginActivity

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            finish()
            return
        }

        val apiService = RetrofitClient.instance
        val authRepository = AuthRepositoryImpl(apiService)
        val profileViewModel = ProfileViewModel(authRepository)

        setContent {
            val navigateToLogin = profileViewModel.navigateToLogin.collectAsState()

            LaunchedEffect(navigateToLogin.value) {
                if (navigateToLogin.value) {
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()
                }
            }

            ProfileScreen(
                userId = userId,
                onBackClick = {
                    val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()
                },
                onEditProfileClick = {
                    profileViewModel.startEditing()
                },
                onDeleteAccountClick = {
                    profileViewModel.deleteAccount(userId)
                },
                viewModel = profileViewModel
            )
        }
    }
}