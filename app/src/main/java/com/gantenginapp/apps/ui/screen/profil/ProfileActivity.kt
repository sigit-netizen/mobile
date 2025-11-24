package com.gantenginapp.apps.ui.screen.profil

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gantenginapp.apps.data.remote.RetrofitClient
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import com.gantenginapp.apps.ui.screen.home.HomeActivity
import com.gantenginapp.apps.R

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil userId dari intent
        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            // Handle error jika userId tidak ditemukan
            finish()
            return
        }

        // ✅ Ambil ApiService dari RetrofitClient.instance
        val apiService = RetrofitClient.instance

        // ✅ Buat instance AuthRepositoryImpl dengan ApiService dari RetrofitClient
        val authRepository = AuthRepositoryImpl(apiService)

        // ✅ Buat instance ProfileViewModel
        val profileViewModel = ProfileViewModel(authRepository)

        setContent {
            ProfileScreen(
                userId = userId,
                onBackClick = {
                    val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
                    startActivity(intent)
                    // Animasi: geser dari kiri ke kanan (register → login)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish() // tutup RegisterActivity

                },
                onRegistStoreClick = {
                    // TODO: Arahkan ke RegisterStoreActivity
                },
                onEditProfileClick = {
                    // TODO: Arahkan ke EditProfileActivity
                },
                viewModel = profileViewModel
            )
        }
    }
}