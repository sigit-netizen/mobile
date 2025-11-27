package com.gantenginapp.apps.ui.screen.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.ui.screen.login.LoginActivity
import com.gantenginapp.apps.ui.screen.profil.ProfileActivity
import com.gantenginapp.apps.ui.screen.registerstore.RegisterStoreActivity
import com.gantenginapp.apps.ui.screen.StoreBarber.BarberStoreActivity// ✅ Ganti nama activity

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var showLogoutDialog by remember { mutableStateOf(false) }

            var showRegisterConfirmation by remember { mutableStateOf(false) }

            var showBackLogoutDialog by remember { mutableStateOf(false) }


            val backPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showBackLogoutDialog = true
                }
            }
            onBackPressedDispatcher.addCallback(this, backPressedCallback)

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
                finishAffinity()
            }

            // ✅ Fungsi untuk menampilkan dialog konfirmasi daftar toko
            fun showRegisterConfirmationDialog() {
                showRegisterConfirmation = true
            }

            // ✅ Fungsi untuk menutup dialog konfirmasi daftar toko
            fun dismissRegisterConfirmation() {
                showRegisterConfirmation = false
            }

            // ✅ Fungsi untuk buka RegisterStoreActivity
            fun goToRegisterStore() {
                showRegisterConfirmation = false // tutup dialog
                val intent = Intent(this@HomeActivity, RegisterStoreActivity::class.java)
                startActivity(intent)
            }

            // ✅ Dialog logout saat back
            if (showBackLogoutDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showBackLogoutDialog = false },
                    title = { androidx.compose.material3.Text("Konfirmasi Logout") },
                    text = { androidx.compose.material3.Text("Apakah Anda yakin ingin logout?") },
                    confirmButton = {
                        androidx.compose.material3.TextButton(
                            onClick = {
                                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                            }
                        ) {
                            androidx.compose.material3.Text("Ya")
                        }
                    },
                    dismissButton = {
                        androidx.compose.material3.TextButton(
                            onClick = { showBackLogoutDialog = false }
                        ) {
                            androidx.compose.material3.Text("Tidak")
                        }
                    }
                )
            }
                val viewModel: HomeViewModel = viewModel()

            HomeScreen(
                onProfileClick = {
                    val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    val userId = sharedPref.getInt("user_id", -1)
                    if (userId != -1) {
                        val intent = Intent(this@HomeActivity, ProfileActivity::class.java).apply {
                            putExtra("USER_ID", userId)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                },
                onDetailClick = {
                    val intent = Intent(this@HomeActivity, BarberStoreActivity::class.java)
                    startActivity(intent)
                },
                onLogoutClick = { viewModel.showLogoutDialog() }, // ✅ Sekarang panggil ViewModel
                onRegisterClick = {
                    val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    val userRole = sharedPref.getString("user_role", "")
                    if (userRole == "admin") {
                        val intent = Intent(this@HomeActivity, RegisterStoreActivity::class.java)
                        startActivity(intent)
                    } else {
                        showRegisterConfirmationDialog()
                    }
                },
                onConfirmLogout = {
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                },
                showRegisterConfirmation = showRegisterConfirmation,
                onDismissRegisterConfirmation = { dismissRegisterConfirmation() },
                onConfirmRegisterStore = { goToRegisterStore() },
                viewModel = viewModel // ✅ Pastikan dikirim
            )
        }
    }
}