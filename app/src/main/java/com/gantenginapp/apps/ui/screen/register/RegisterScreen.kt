// app/src/main/java/com/gantenginapp/apps/ui/screen/register/RegisterScreen.kt
package com.gantenginapp.apps.ui.screen.register

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import com.gantenginapp.apps.ui.theme.ColorCustom

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val email by viewModel.email.collectAsState()
    val noHp by viewModel.noHp.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val passwordVisible by viewModel.passwordVisible.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val usernameError by viewModel.usernameError.collectAsState()
    val noHpError by viewModel.noHpError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val showErrorDialog by viewModel.showErrorDialog.collectAsState()

    // ✅ AlertDialog untuk sukses
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSuccessDialog() },
            title = { Text("Registrasi Berhasil") },
            text = { Text("Akun Anda telah berhasil dibuat. Silakan login untuk melanjutkan.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissSuccessDialog()
                    onRegisterSuccess()
                }) {
                    Text("OK")
                }
            }
        )
    }

    // ✅ AlertDialog untuk error
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissErrorDialog() },
            title = { Text("Registrasi Gagal") },
            text = { Text(errorMessage ?: "Terjadi kesalahan saat registrasi.") },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissErrorDialog() }) {
                    Text("OK")
                }
            }
        )
    }

    RegisterScreenContent(
        username = username,
        password = password,
        email = email,
        noHp = noHp,
        isLoading = isLoading,
        passwordVisible = passwordVisible,
        usernameError = usernameError,
        noHpError = noHpError,
        emailError = emailError,
        passwordError = passwordError,
        onUsernameChange = { viewModel.onUsernameChange(it) },
        onNoHpChange = { viewModel.onNoHpChange(it) },
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        onRegisterClick = {
            Log.d("RegisterScreen", "onRegisterClick dipanggil")  // ✅ Sudah ada
            viewModel.performRegister()
        },
        onBackClick = onBackClick
    )
}

@Composable
fun RegisterScreenContent(
    username: String,
    password: String,
    email: String,
    noHp: String,
    isLoading: Boolean,
    passwordVisible: Boolean,
    usernameError: String?,
    noHpError: String?,
    emailError: String?,
    passwordError: String?,
    onUsernameChange: (String) -> Unit,
    onNoHpChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // ✅ Hapus Box wrapper dan overlay loading
    Column(
        modifier = modifier
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
                isError = usernameError != null,
                supportingText = {
                    if (usernameError != null) {
                        Text(
                            text = usernameError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            TextField(
                value = noHp,
                onValueChange = onNoHpChange,
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
                isError = noHpError != null,
                supportingText = {
                    if (noHpError != null) {
                        Text(
                            text = noHpError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = onEmailChange,
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
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(
                            text = emailError,
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
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) {
                        Text(
                            text = passwordError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    val image = if (passwordVisible) {
                        Icons.Default.Visibility
                    } else {
                        Icons.Default.VisibilityOff
                    }
                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(
                            imageVector = image,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    Log.d("RegisterScreen", "Button onClick dipanggil")  // ✅ Tambahkan log
                    onRegisterClick()  // ✅ Panggil fungsi
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading // ✅ Nonaktifkan tombol saat loading
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

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to login", color = ColorCustom.link)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreenContent(
        username = "",
        password = "",
        email = "",
        noHp = "",
        isLoading = false,
        passwordVisible = false,
        usernameError = null,
        noHpError = null,
        emailError = null,
        passwordError = null,
        onUsernameChange = {},
        onNoHpChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onRegisterClick = {},
        onBackClick = {}
    )
}