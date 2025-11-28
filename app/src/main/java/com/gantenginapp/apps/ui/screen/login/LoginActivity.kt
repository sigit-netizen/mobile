package com.gantenginapp.apps.ui.screen.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gantenginapp.apps.data.remote.ApiService
import com.gantenginapp.apps.data.remote.RetrofitClient // ✅ Import RetrofitClient
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import com.gantenginapp.apps.ui.screen.register.RegisterActivity
import com.gantenginapp.apps.R // ✅ Tambahkan ini agar bisa mengakses R.anim
import com.gantenginapp.apps.ui.screen.home.HomeActivity
import com.gantenginapp.apps.data.local.UserPreferences
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService: ApiService = RetrofitClient.instance
        // ✅ Buat instance AuthRepositoryImpl dengan ApiService dari RetrofitClient
        val authRepository = AuthRepositoryImpl(apiService)
        // ✅ Buat instance LoginViewModel
        val prefs = UserPreferences(this)
        val loginViewModel = LoginViewModel(authRepository, this, prefs) // Kirim context

        setContent {
            LoginScreen(
                onLoginSuccess = {
                    // ✅ Login sukses → pindah ke HomeActivity
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity() // Tutup semua activity sebelumnya
                },
                onRegisterClick = {
                    // ✅ Buka RegisterActivity dengan animasi geser ke kiri
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                    // Animasi: geser dari kanan ke kiri (login → register)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                },
                onBackClick = {
                    // ✅ Keluar dari aplikasi sepenuhnya
                    finishAffinity() // Menutup semua activity dalam task ini
                },
                viewModel = loginViewModel // Gunakan instance yang sudah dibuat
            )
        }
    }
}