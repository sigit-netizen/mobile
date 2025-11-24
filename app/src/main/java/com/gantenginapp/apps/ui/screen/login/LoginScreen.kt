// File: app/src/main/java/com/gantenginapp/apps/ui/screen/login/LoginScreen.kt
package com.gantenginapp.apps.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import com.gantenginapp.apps.domain.model.User
import com.gantenginapp.apps.ui.theme.ColorCustom

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    // ✅ Ambil error validasi lokal dari ViewModel
    val usernameLocalError by viewModel.usernameLocalError.collectAsState()
    val passwordLocalError by viewModel.passwordLocalError.collectAsState()

    LoginScreenContent(
        username = username,
        password = password,
        usernameLocalError = usernameLocalError, // ✅ Kirim error ke content
        passwordLocalError = passwordLocalError, // ✅ Kirim error ke content
        errorMessage = errorMessage,
        successMessage = successMessage,
        isLoading = isLoading,
        onUsernameChange = {
            viewModel.onUsernameChange(it)
            viewModel.clearStatus()
        },
        onPasswordChange = {
            viewModel.onPasswordChange(it)
            viewModel.clearStatus()
        },
        onLoginClick = {
            viewModel.performLogin {
                onLoginSuccess()
            }
        },
        onRegisterClick = onRegisterClick,
        onBackClick = onBackClick
    )
}

@Composable
fun LoginScreenContent(
    username: String,
    password: String,
    usernameLocalError: String?, // ✅ Terima error validasi lokal
    passwordLocalError: String?, // ✅ Terima error validasi lokal
    errorMessage: String?,
    successMessage: String?,
    isLoading: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorCustom.bg)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "Keluar",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text("?", fontSize = 28.sp)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gantengin),
                    contentDescription = "App logo",
                    modifier = Modifier.size(260.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .background(
                    color = ColorCustom.dark,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .fillMaxHeight()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Login",
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = ColorCustom.bg,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(16.dp))

            TextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Username") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = ColorCustom.black,
                    unfocusedTextColor = ColorCustom.black,
                    cursorColor = ColorCustom.black,
                    focusedIndicatorColor = ColorCustom.black,
                    unfocusedIndicatorColor = ColorCustom.black,
                    focusedLabelColor = ColorCustom.black,
                    unfocusedLabelColor = ColorCustom.black,
                    unfocusedContainerColor = ColorCustom.bg,
                    focusedContainerColor = ColorCustom.bg,
                ),
                isError = usernameLocalError != null, // ✅ Tandai error jika ada error validasi lokal (kosong atau dari API)
                supportingText = { // ✅ Tampilkan pesan error validasi lokal
                    if (usernameLocalError != null) {
                        Text(
                            text = usernameLocalError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = ColorCustom.black,
                    unfocusedTextColor = ColorCustom.black,
                    cursorColor = ColorCustom.black,
                    focusedIndicatorColor = ColorCustom.black,
                    unfocusedIndicatorColor = ColorCustom.black,
                    focusedLabelColor = ColorCustom.black,
                    unfocusedLabelColor = ColorCustom.black,
                    unfocusedContainerColor = ColorCustom.bg,
                    focusedContainerColor = ColorCustom.bg,
                ),
                isError = passwordLocalError != null, // ✅ Tandai error jika ada error validasi lokal (kosong atau dari API)
                supportingText = { // ✅ Tampilkan pesan error validasi lokal
                    if (passwordLocalError != null) {
                        Text(
                            text = passwordLocalError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            // ✅ Tampilkan pesan error GLOBAL (dari ViewModel - validasi lokal gagal, atau error umum dari API/jaringan)
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall // Gunakan style kecil agar tidak terlalu dominan
                )
                Spacer(Modifier.height(4.dp)) // Sedikit jarak setelah pesan error
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login")
                }
            }

            // ✅ Tampilkan pesan sukses
            successMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color(0xFF4CAF50)) // Hijau sukses
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Go to register", color = ColorCustom.link)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TryLoginScreenPreview() {
    LoginScreen(
        onLoginSuccess = {},
        onRegisterClick = {},
        onBackClick = {}
    )
}