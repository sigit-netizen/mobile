package com.gantenginapp.apps.pages.register

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.gantenginapp.apps.R
import com.gantenginapp.apps.model.User
import com.gantenginapp.apps.ui.theme.ColorCustom

@Composable
fun RegisterScreen(
    onRegisterSuccess: (User) -> Unit,
    onBackClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember {mutableStateOf("")}
    var noHp by remember {mutableStateOf("")}

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
                onValueChange = { username = it },
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
            // No Hp
            TextField(
                value = noHp,
                onValueChange = { noHp = it },
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
                )
            )

            Spacer(Modifier.height(8.dp))

            // Email
            TextField(
                value = email,
                onValueChange = { email = it },
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
                )
            )

            Spacer(Modifier.height(8.dp))


            // Password
            TextField(
                value = password,
                onValueChange = { password = it },
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

            // Tombol Register
            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }

            Spacer(Modifier.height(8.dp))

            // Tombol balik ke login
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
    RegisterScreen(
        onRegisterSuccess = {},
        onBackClick = {}
    )
}
