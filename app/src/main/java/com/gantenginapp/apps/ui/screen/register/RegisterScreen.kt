// File: RegisterScreen.kt
package com.gantenginapp.apps.ui.screen.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel // ✅ Import ini
import com.gantenginapp.apps.R
import com.gantenginapp.apps.domain.model.User
import com.gantenginapp.apps.ui.theme.ColorCustom

@Composable
fun RegisterScreen(
    onRegisterSuccess: (User) -> Unit,
    onBackClick: () -> Unit,
    onRegistrationComplete: () -> Unit // ✅ Harus ada
) {
    // ✅ Gunakan ViewModel
    val viewModel: RegisterViewModel = viewModel()

    // ✅ Ambil state dari ViewModel (termasuk error lokal)
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val email by viewModel.email.collectAsState()
    val noHp by viewModel.noHp.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    // ✅ Ambil error lokal
    val usernameError by viewModel.usernameError.collectAsState()
    val noHpError by viewModel.noHpError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorCustom.bg)
    ) {
        // Bagian Atas (Logo dan Header)
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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

        // Bagian Bawah (Form Register)
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
                "Register",
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = ColorCustom.bg,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(16.dp))

            // Username
            TextField(
                value = username,
                onValueChange = { viewModel.onUsernameChange(it) }, // ✅ Gunakan ViewModel
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
                isError = usernameError != null, // ✅ Tandai error
                supportingText = { // ✅ Tampilkan pesan error atau helper text
                    if (usernameError != null) {
                        Text(
                            text = usernameError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    // Jika tidak ada error, supportingText tetap perlu ada, bisa kosong
                    // Tapi karena kita hanya ingin tampilkan saat error, kita biarkan 'if' saja
                    // Jika ingin placeholder saat tidak error, tambahkan else {}
                }
            )

            Spacer(Modifier.height(8.dp))

            // No Hp
            TextField(
                value = noHp,
                onValueChange = { viewModel.onNoHpChange(it) }, // ✅ Gunakan ViewModel
                label = { Text("NoHp") },
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
                isError = noHpError != null, // ✅ Tandai error
                supportingText = { // ✅ Tampilkan pesan error atau helper text
                    if (noHpError != null) {
                        Text(
                            text = noHpError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            // Email
            TextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) }, // ✅ Gunakan ViewModel
                label = { Text("Email") },
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
                isError = emailError != null, // ✅ Tandai error
                supportingText = { // ✅ Tampilkan pesan error atau helper text
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            // Password
            TextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) }, // ✅ Gunakan ViewModel
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
                isError = passwordError != null, // ✅ Tandai error
                supportingText = { // ✅ Tampilkan pesan error atau helper text
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            // Tombol Register
            Button(
                onClick = {
                    // ✅ Panggil ViewModel untuk memproses registrasi
                    // Jangan langsung panggil onRegistrationComplete() di sini
                    viewModel.performRegister { user ->
                        onRegisterSuccess(user)
                        onRegistrationComplete() // ✅ Panggil di sini, setelah sukses dari ViewModel
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading // ✅ Nonaktifkan saat loading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Register")
                }
            }

            // ✅ Tampilkan pesan error GLOBAL (dari ViewModel - backend, jaringan)
            errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            // ✅ Tampilkan pesan sukses (dari ViewModel)
            successMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color(0xFF4CAF50)) // hijau
            }

            Spacer(Modifier.height(8.dp))

            // Tombol balik ke login
            TextButton( // ✅ Pastikan struktur ini benar
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to login", color = ColorCustom.link) // 'content' untuk TextButton adalah composable di dalam {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(
        onRegisterSuccess = { },
        onBackClick = { },
        onRegistrationComplete = { } // ✅ Harus ada di preview juga
    )
}