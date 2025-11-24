// File: RegisterActivity.kt
package com.gantenginapp.apps.ui.screen.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.gantenginapp.apps.ui.screen.login.LoginActivity // ✅ Import yang benar
import com.gantenginapp.apps.R

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var navigateToLogin by remember { mutableStateOf(false) }

            RegisterScreen(
                onRegisterSuccess = { user ->
                    // ✅ Set state untuk navigasi ke login
                    navigateToLogin = true
                },
                onBackClick = {
                    // ✅ Kembali ke LoginActivity dengan animasi geser ke kanan
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish() // tutup RegisterActivity agar tidak bisa kembali ke sini via back button
                },
                onRegistrationComplete = { // ✅ Tambahkan baris ini
                    // Panggil saat tombol Register diklik di RegisterScreen
                    navigateToLogin = true
                }
            )

            // ✅ Efek untuk navigasi setelah sukses registrasi
            LaunchedEffect(navigateToLogin) {
                if (navigateToLogin) {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    // Animasi: geser dari kiri ke kanan (register → login)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish() // tutup RegisterActivity
                    navigateToLogin = false
                }
            }
        }
    }
}