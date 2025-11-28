package com.gantenginapp.apps.ui.screen.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.ui.screen.login.LoginActivity
import com.gantenginapp.apps.ui.screen.profil.ProfileActivity
import com.gantenginapp.apps.ui.screen.registstore.RegistStoreActivity
import com.gantenginapp.apps.ui.screen.StoreBarber.BarberStoreActivity
import com.gantenginapp.apps.ui.screen.adminstore.AdminStoreActivity
import com.gantenginapp.apps.data.repository.UserRepository
import com.gantenginapp.apps.data.local.UserPreferences
import com.gantenginapp.apps.data.remote.ApiService
import com.gantenginapp.apps.data.repository.DataRefresher
import com.gantenginapp.apps.data.remote.RetrofitClient
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.ui.unit.dp

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Buat repository di sini
        val prefs = UserPreferences(this)
        val dataRefresher = DataRefresher(UserRepository(prefs), RetrofitClient.instance)


        setContent {
            var showLogoutDialog by remember { mutableStateOf(false) }
            val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(UserRepository(prefs), dataRefresher))
            val user by viewModel.user.collectAsState()
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


            fun showRegisterConfirmationDialog() {
                if (user?.role == "user") {
                    showRegisterConfirmation = true
                } else {
                    showRegisterConfirmation = false
                }
            }

            // ✅ Fungsi untuk menutup dialog konfirmasi daftar toko
            fun dismissRegisterConfirmation() {
                showRegisterConfirmation = false
            }

            // ✅ Fungsi untuk buka RegisterStoreActivity
            fun goToRegisterStore() {
                showRegisterConfirmation = false // tutup dialog
                if (user?.role  == "admin-store") {
                    val intent = Intent(this@HomeActivity, AdminStoreActivity::class.java)
                    startActivity(intent)
                } else {

                    val intent = Intent(this@HomeActivity, RegistStoreActivity::class.java)
                    startActivity(intent)
                }

            }


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

                val isLoading by viewModel.isRefreshingLoading.collectAsState()
                val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
                SwipeRefresh(state = swipeRefreshState,
                    onRefresh = {
                    viewModel.loadDataStoreAndUser()
                        viewModel.loadData()
                                }, indicatorPadding = PaddingValues(top = 16.dp)) {
                    HomeScreen(
                        onProfileClick = {
                            val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                            val userId = sharedPref.getString("user_id", null)
                            if (userId != null) {
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
                        onLogoutClick = { viewModel.showLogoutDialog() },
                        onRegisterClick = {
                            showRegisterConfirmationDialog()
                        },
                        onConfirmLogout = {
                            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        },
                        showRegisterConfirmation = showRegisterConfirmation,
                        onDismissRegisterConfirmation = { dismissRegisterConfirmation() },
                        onConfirmRegisterStore = { goToRegisterStore() },
                        viewModel = viewModel //
                    )
                }

        }
    }
}