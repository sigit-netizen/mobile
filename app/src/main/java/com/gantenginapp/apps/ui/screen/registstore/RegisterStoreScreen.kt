package com.gantenginapp.apps.ui.screen.registstore

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import com.gantenginapp.apps.ui.theme.ColorCustom

@Composable
fun RegisterStoreScreen(
    onBackClick: () -> Unit,
    viewModel: RegisterStoreViewModel,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var alamatToko by remember { mutableStateOf("") }

    // Observe status
    val isLoading by viewModel.isLoading.collectAsState()
    val successMsg by viewModel.success.collectAsState()
    val errorMsg by viewModel.error.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }


    // --- Ketika sukses daftar toko ---
    LaunchedEffect(successMsg) {
        if (successMsg != null) {
            showSuccessDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorCustom.bg)
    ) {

        // Bagian Atas (Header + Logo)
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
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gantengin),
                    contentDescription = "App logo",
                    modifier = Modifier.size(260.dp)
                )
            }
        }

        // Bagian Form
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
                "PENDAFTARAN TOKO",
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = ColorCustom.bg,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(40.dp))

            // Nama Toko
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Toko") },
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

            // Alamat Toko
            TextField(
                value = alamatToko,
                onValueChange = { alamatToko = it },
                label = { Text("Alamat Toko") },
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

            // Tombol Daftar Toko
            Button(
                onClick = {
                    viewModel.registerStore(
                        name = name.trim(),
                        alamat = alamatToko.trim(),
                        onSuccess = {} // tidak dipakai lagi
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = ColorCustom.bg,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Register")
                }
            }

            Spacer(Modifier.height(12.dp))

            //
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("Berhasil Daftar Toko", color = ColorCustom.dark) },
                    text = { Text("Toko kamu berhasil terdaftar!", color = ColorCustom.black)  },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showSuccessDialog = false
                                onRegisterSuccess()   // baru balik ke beranda
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    containerColor = ColorCustom.bg,
                    titleContentColor = ColorCustom.bg,
                    textContentColor = ColorCustom.bg
                )
            }

            // Error Message
            if (errorMsg != null) {
                Text(
                    text = errorMsg ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterStoreScreen() {

    RegisterStoreScreen(
        onBackClick = {},
        viewModel = viewModel (),
        onRegisterSuccess = {}
    )
}
