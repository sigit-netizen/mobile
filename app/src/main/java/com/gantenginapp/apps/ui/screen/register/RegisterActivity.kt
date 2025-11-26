// File: RegisterActivity.kt
package com.gantenginapp.apps.ui.screen.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.data.remote.RetrofitClient
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import com.gantenginapp.apps.ui.screen.login.LoginActivity
import com.gantenginapp.apps.R

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inisialisasi ApiService dan Repository
        val apiService = RetrofitClient.instance
        val authRepository = AuthRepositoryImpl(apiService)

        setContent {
            var navigateToLogin by remember { mutableStateOf(false) }

            // ✅ Buat ViewModel secara manual dan kirim ke RegisterScreen
            val registerViewModel: RegisterViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return RegisterViewModel(authRepository) as T
                    }
                }
            )

            RegisterScreen(
                onRegisterSuccess = {
                    // ✅ Registrasi sukses (dari dialog), arahkan ke login
                    navigateToLogin = true
                },
                onBackClick = {
                    // ✅ Kembali ke LoginActivity dengan animasi geser ke kanan
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()
                },
                viewModel = registerViewModel // ✅ Hapus parameter onRegistrationComplete
            )

            // ✅ Efek untuk navigasi setelah sukses registrasi
            LaunchedEffect(navigateToLogin) {
                if (navigateToLogin) {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()
                    navigateToLogin = false
                }
            }
        }
    }
}