package com.gantenginapp.apps.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import com.gantenginapp.apps.ui.screen.profil.ProfileActivity
import android.content.Intent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onDetailClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRegisterClick: () -> Unit,
    showLogoutDialog: Boolean,
    onDismissLogoutDialog: () -> Unit,
    onConfirmLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val username by viewModel.username.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val data by viewModel.data.collectAsState()

    HomeContent(
        username = username,
        isLoading = isLoading,
        data = data, // ✅ Fix: parameter 'data' sudah didefinisikan
        onProfileClick = onProfileClick,
        onDetailClick = onDetailClick,
        onLogoutClick = onLogoutClick,
        onRegisterClick = onRegisterClick,
        showLogoutDialog = showLogoutDialog,
        onDismissLogoutDialog = onDismissLogoutDialog,
        onConfirmLogout = onConfirmLogout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    username: String,
    isLoading: Boolean,
    data: List<String>, // ✅ Fix: Tambahkan tipe data
    onProfileClick: () -> Unit,
    onDetailClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRegisterClick: () -> Unit,
    showLogoutDialog: Boolean,
    onDismissLogoutDialog: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    val context = LocalContext.current

    // ✅ Tampilkan dialog logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = onDismissLogoutDialog,
            title = { Text("Konfirmasi Logout") },
            text = { Text("Apakah Anda yakin ingin logout?") },
            confirmButton = {
                Button(onClick = onConfirmLogout) {
                    Text("Ya")
                }
            },
            dismissButton = {
                Button(onClick = onDismissLogoutDialog) {
                    Text("Tidak")
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.White,
        // Header
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.background(Color.White),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.gantengin),
                            contentDescription = "App logo",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
//                                .border(1.dp, Color.Gray, CircleShape)
                                .height(40.dp)
                        )

                        TextField(
                            value = "",
                            onValueChange = { /* Handle value change */ },
                            label = { Text(text = "Cari toko....", fontSize = 12.sp) }, // Sesuaikan ukuran label
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            modifier = Modifier
                                .width(250.dp)
                                .heightIn(min = 40.dp), // Minimal 40.dp agar tetap bisa diklik
                            shape = RoundedCornerShape(30.dp),
                            textStyle = TextStyle(fontSize = 20.sp), // ✅ Fix: Gunakan TextStyle
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,   // Hilangkan garis bawah
                                unfocusedIndicatorColor = Color.Transparent, // Hilangkan garis bawah
                                disabledIndicatorColor = Color.Transparent   // Hilangkan garis bawah
                            )
                        )

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
//                                .background(Color.DarkGray.copy(alpha = 0.4f))
                                .clickable {
                                    onProfileClick() // ✅ Panggil fungsi onProfileClick
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            // Ganti Icon menjadi Image
                            Image(
                                painter = painterResource(id = R.drawable.ic_profil), // Ganti dengan nama gambar kamu
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(24.dp) // Sesuaikan ukuran gambar
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop // Agar gambar tidak terdistorsi
                            )
                        }

                        // ❌ Tombol Logout dihapus dari sini
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                )
            )
        },
        // Footer
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.background(Color.White),
                containerColor = Color.White,
                contentColor = Color.Black,
                tonalElevation = 4.dp
            ) {
                Text("Copyright © 2023 Gantengin App")
            }
        },
        // FAB
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle FAB click */ }) {
                Text("+")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(data.size) { index ->
                HorizontalCardPlaceholder(
                    onDetailClick = onDetailClick
                )
            }
        }
    }
}

@Composable
fun HorizontalCardPlaceholder(
    onDetailClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                // Ini Icon
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Placeholder",
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(32.dp)
                )
            }
            // Text Disebelah kanan
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nama Toko",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "5/5",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Rp.10000"
                    )

                    Button(
                        onClick = onDetailClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Cek Detail") // ✅ Fix: @Composable function
                    }
                }
            }
        }
    }
}

// Main preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeContentPreview() {
    HomeContent(
        username = "Ananda",
        isLoading = false,
        data = listOf("Item 1", "Item 2"), // ✅ Fix: parameter 'data' diberikan
        onProfileClick = {},
        onDetailClick = {},
        onLogoutClick = {},
        onRegisterClick = {},
        showLogoutDialog = false,
        onDismissLogoutDialog = {},
        onConfirmLogout = {}
    )
}