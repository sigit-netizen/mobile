package com.gantenginapp.apps.ui.screen.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.ui.screen.login.LoginActivity
import com.gantenginapp.apps.ui.screen.profil.ProfileActivity
import com.gantenginapp.apps.ui.screen.register.RegisterActivity

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ✅ State untuk menampilkan dialog logout
            var showLogoutDialog by remember { mutableStateOf(false) }

            // ✅ Fungsi untuk menampilkan dialog logout
            fun showLogoutConfirmation() {
                showLogoutDialog = true
            }

            // ✅ Fungsi untuk menutup dialog logout
            fun dismissLogoutDialog() {
                showLogoutDialog = false
            }

            // ✅ Fungsi untuk logout
            fun performLogout() {
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity() // Tutup semua activity kecuali login
            }

            // ✅ Tangani tombol kembali
            val backPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showLogoutConfirmation()
                }
            }
            onBackPressedDispatcher.addCallback(this, backPressedCallback)

            HomeScreen(
                onProfileClick = {
                    // ✅ Arahkan ke ProfileActivity
                    val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                    startActivity(intent)
                },
                onDetailClick = {
                    // Contoh: navigasi ke DetailScreen
                },
                onLogoutClick = { performLogout() }, // Panggil fungsi logout
                onRegisterClick = {
                    // ✅ Buka RegisterActivity
                    val intent = Intent(this@HomeActivity, RegisterActivity::class.java)
                    startActivity(intent)
                },
                showLogoutDialog = showLogoutDialog, // ✅ Tambahkan parameter
                onDismissLogoutDialog = { dismissLogoutDialog() }, // ✅ Tambahkan parameter
                onConfirmLogout = { performLogout() }, // ✅ Tambahkan parameter
                viewModel = viewModel() // ✅ Gunakan ViewModel
            )
        }
    }
}