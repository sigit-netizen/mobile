// File: app/src/main/java/com/gantenginapp/apps/ui/screen/login/LoginScreen.kt
package com.gantenginapp.apps.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    val usernameLocalError by viewModel.usernameLocalError.collectAsState()
    val passwordLocalError by viewModel.passwordLocalError.collectAsState()

    val passwordVisible by viewModel.passwordVisible.collectAsState()

    LoginScreenContent(
        username = username,
        password = password,
        passwordVisible = passwordVisible,
        usernameLocalError = usernameLocalError,
        passwordLocalError = passwordLocalError,
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
        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
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
    passwordVisible: Boolean,
    usernameLocalError: String?,
    passwordLocalError: String?,
    errorMessage: String?,
    successMessage: String?,
    isLoading: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                "LOGIN",
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
                isError = usernameLocalError != null,
                supportingText = {
                    if (usernameLocalError != null) {
                        Text(
                            text = usernameLocalError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            // ✅ Ganti TextField password dengan ikon mata
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
                isError = passwordLocalError != null,
                supportingText = {
                    if (passwordLocalError != null) {
                        Text(
                            text = passwordLocalError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                // ✅ Visual transformation dinamis
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                // ✅ Tambahkan trailing icon (mata)
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

            Spacer(Modifier.height(8.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(4.dp))
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

            successMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color(0xFF4CAF50))
            }

            Spacer(Modifier.height(8.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Do you want to create an account ? ", color = ColorCustom.bg)
                TextButton(
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Text("Go to register", color = ColorCustom.link)
                }
            }

        }
    }
}

//region Preview
@Preview(showBackground = true, name = "Login - Default")
@Composable
fun PreviewLoginScreen_Default() {
    LoginScreenContent(
        username = "",
        password = "",
        passwordVisible = false,
        usernameLocalError = null,
        passwordLocalError = null,
        errorMessage = null,
        successMessage = null,
        isLoading = false,
        onUsernameChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onLoginClick = {},
        onRegisterClick = {},
        onBackClick = {}
    )
}

@Preview(showBackground = true, name = "Login - Error Username")
@Composable
fun PreviewLoginScreen_UsernameError() {
    LoginScreenContent(
        username = "user",
        password = "123",
        passwordVisible = false,
        usernameLocalError = "Username minimal 4 karakter",
        passwordLocalError = null,
        errorMessage = null,
        successMessage = null,
        isLoading = false,
        onUsernameChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onLoginClick = {},
        onRegisterClick = {},
        onBackClick = {}
    )
}

@Preview(showBackground = true, name = "Login - Error Password")
@Composable
fun PreviewLoginScreen_PasswordError() {
    LoginScreenContent(
        username = "user123",
        password = "123",
        passwordVisible = false,
        usernameLocalError = null,
        passwordLocalError = "Password minimal 6 karakter",
        errorMessage = null,
        successMessage = null,
        isLoading = false,
        onUsernameChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onLoginClick = {},
        onRegisterClick = {},
        onBackClick = {}
    )
}

@Preview(showBackground = true, name = "Login - Global Error")
@Composable
fun PreviewLoginScreen_GlobalError() {
    LoginScreenContent(
        username = "user123",
        password = "password123",
        passwordVisible = false,
        usernameLocalError = null,
        passwordLocalError = null,
        errorMessage = "Username atau password salah",
        successMessage = null,
        isLoading = false,
        onUsernameChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onLoginClick = {},
        onRegisterClick = {},
        onBackClick = {}
    )
}

@Preview(showBackground = true, name = "Login - Loading")
@Composable
fun PreviewLoginScreen_Loading() {
    LoginScreenContent(
        username = "user123",
        password = "password123",
        passwordVisible = false,
        usernameLocalError = null,
        passwordLocalError = null,
        errorMessage = null,
        successMessage = null,
        isLoading = true,
        onUsernameChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onLoginClick = {},
        onRegisterClick = {},
        onBackClick = {}
    )
}

@Preview(showBackground = true, name = "Login - Success")
@Composable
fun PreviewLoginScreen_Success() {
    LoginScreenContent(
        username = "user123",
        password = "password123",
        passwordVisible = false,
        usernameLocalError = null,
        passwordLocalError = null,
        errorMessage = null,
        successMessage = "Login berhasil!",
        isLoading = false,
        onUsernameChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onLoginClick = {},
        onRegisterClick = {},
        onBackClick = {}
    )
}
//endregion