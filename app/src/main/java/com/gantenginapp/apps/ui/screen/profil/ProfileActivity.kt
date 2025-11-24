    package com.gantenginapp.apps.ui.screen.profil

    import android.content.Intent
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.gantenginapp.apps.ui.screen.home.HomeActivity
    import com.gantenginapp.apps.ui.screen.login.LoginActivity

    class ProfileActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                ProfileScreen(
                    onBackClick = {
                        // ✅ Arahkan ke HomeActivity
                        val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onRegistStoreClick = {
                        // ✅ Arahkan ke RegisterStoreActivity
                    },
                    onEditProfileClick = {
                        // ✅ Arahkan ke EditProfileActivity
                    },
                    viewModel = viewModel()
                )
            }
        }
    }