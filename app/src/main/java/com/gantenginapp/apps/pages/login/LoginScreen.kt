package com.gantenginapp.apps.pages.login

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gantenginapp.apps.R
import com.gantenginapp.apps.model.User
import com.gantenginapp.apps.ui.theme.ColorCustom
import kotlinx.coroutines.launch
import com.gantenginapp.apps.remote.RetrofitClient
import com.gantenginapp.apps.remote.ApiService
import com.gantenginapp.apps.remote.LoginRequest



@Composable
fun LoginScreen(
    registeredUser: User?,
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LoginScreenContent(
        username = username,
        password = password,
        errorMessage = errorMessage,
        onUsernameChange = { username = it },
        onPasswordChange = { password = it },
        onLoginClick = {
            coroutineScope.launch {
                try {
                    isLoading = true

                    val api = RetrofitClient.instance
                    val response = api.login(LoginRequest(username, password))

                    if (response.status == "success" && response.data != null) {
                        onLoginSuccess(response.data.username)
                    } else {
                        errorMessage = "Login failed: ${response.message}"
                    }

                } catch (e: Exception) {
                    errorMessage = "Login failed: ${e.message}"
                } finally {
                    isLoading = false
                }

            }


        },
        onRegisterClick = onRegisterClick
    )
}

@Composable
fun LoginScreenContent(
    username: String,
    password: String,
    errorMessage: String?,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
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
                IconButton(onClick = { /* back */ }) {
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

        // Bagian Bawah (Form)
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
                )
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
                )
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
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
        registeredUser = User(id = "",username = "test", password = "123", noHp = "1234", role = "user", email = "Kosong"),
        onLoginSuccess = {},
        onRegisterClick = {}
    )
}
