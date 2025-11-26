package com.gantenginapp.apps.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onDetailClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onConfirmLogout: () -> Unit,
    showRegisterConfirmation: Boolean,
    onDismissRegisterConfirmation: () -> Unit,
    onConfirmRegisterStore: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val username by viewModel.username.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val stores by viewModel.filteredStores.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsState()

    HomeContent(
        username = username,
        isLoading = isLoading,
        stores = stores,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onProfileClick = onProfileClick,
        onDetailClick = onDetailClick,
        onLogoutClick = onLogoutClick,
        onRegisterClick = onRegisterClick,
        showLogoutDialog = showLogoutDialog,
        onDismissLogoutDialog = viewModel::dismissLogoutDialog,
        onConfirmLogout = onConfirmLogout,
        showRegisterConfirmation = showRegisterConfirmation,
        onDismissRegisterConfirmation = onDismissRegisterConfirmation,
        onConfirmRegisterStore = onConfirmRegisterStore
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    username: String,
    isLoading: Boolean,
    stores: List<StoreItem>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onProfileClick: () -> Unit,
    onDetailClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRegisterClick: () -> Unit,
    showLogoutDialog: Boolean,
    onDismissLogoutDialog: () -> Unit,
    onConfirmLogout: () -> Unit,
    showRegisterConfirmation: Boolean,
    onDismissRegisterConfirmation: () -> Unit,
    onConfirmRegisterStore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedMenu by remember { mutableStateOf("home")}
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = onDismissLogoutDialog,
            title = { Text("Konfirmasi Logout") },
            text = { Text("Apakah Anda yakin ingin logout?") },
            confirmButton = {
                TextButton(onClick = onConfirmLogout) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissLogoutDialog) {
                    Text("Tidak")
                }
            }
        )
    }

    if (showRegisterConfirmation) {
        AlertDialog(
            onDismissRequest = onDismissRegisterConfirmation,
            title = { Text("Konfirmasi Daftar Toko") },
            text = { Text("Anda belum memiliki toko. Apakah Anda ingin mendaftarkan toko?") },
            confirmButton = {
                TextButton(onClick = onConfirmRegisterStore) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRegisterConfirmation) {
                    Text("Tidak")
                }
            }
        )
    }



    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // ✅ Logo: ubah jadi 40.dp agar sejajar
                        Image(
                            painter = painterResource(id = R.drawable.ic_toko),
                            contentDescription = "App logo",
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .size(40.dp) // 🔸 Dari 30.dp → 40.dp
                                .clip(CircleShape)
                                .clickable { onRegisterClick() }
                        )

                        // ✅ SearchBar yang diperbaiki
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = {
                                onSearchQueryChange(it)
                                if (!isSearchActive && it.isNotBlank()) {
                                    isSearchActive = true
                                }
                            },
                            onSearch = { query ->
                                onSearchQueryChange(query)
                                isSearchActive = false
                            },
                            active = isSearchActive,
                            onActiveChange = { isSearchActive = it },
                            modifier = Modifier
                                .height(250.dp)
                                .width(250.dp) // 🔸 Ganti dari fillMaxWidth ke lebar tetap
                                .clip(RoundedCornerShape(50.dp)),
                            placeholder = {
                                Text(
                                    "Cari toko...",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 14.sp, // 🔸 Naikkan dari 10.sp → 14.sp
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(25.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { onSearchQueryChange("") },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear search",
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 10.sp // 🔸 Sama dengan placeholder
                                )
                            ) {
                                // Kosong
                            }
                        }

                        // ✅ Profil: ubah jadi 40.dp agar sejajar
                        Box(
                            modifier = Modifier
                                .padding(end = 15.dp)
                                .size(40.dp) // 🔸 Dari 30.dp → 40.dp
                                .clip(CircleShape)
                                .clickable { onProfileClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_profil),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                NavigationBarItem(
                    selected = selectedMenu == "home",
                    onClick = { selectedMenu = "home" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Blue,          // warna icon saat aktif
                        unselectedIconColor = Color.Gray,        // warna icon saat nonaktif
                        selectedTextColor = Color.Blue,          // warna label saat aktif
                        unselectedTextColor = Color.Gray,        // warna label saat nonaktif
                        indicatorColor = Color(0xFFE0E0E0)       // warna background bubble saat aktif
                    )
                )
                NavigationBarItem(
                    selected = selectedMenu == "store",
                    onClick = { selectedMenu = "store" },
                    icon = { Icon(Icons.Default.Adb, contentDescription = "Toko") },
                    label = { Text("Toko") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Blue,          // warna icon saat aktif
                        unselectedIconColor = Color.Gray,        // warna icon saat nonaktif
                        selectedTextColor = Color.Blue,          // warna label saat aktif
                        unselectedTextColor = Color.Gray,        // warna label saat nonaktif
                        indicatorColor = Color(0xFFE0E0E0)       // warna background bubble saat aktif
                    )
                )
                }
            }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            when (selectedMenu) {

                "home" -> {
                    // Halaman Home
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(stores) { store ->
                            HorizontalCardPlaceholder(
                                storeName = store.name,
                                address = store.address,
                                price = store.price,
                                status = store.status,
                                onDetailClick = onDetailClick
                            )
                        }
                    }
                }

                "store" -> {
                    // Halaman Store / AI
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Halaman AI",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = onRegisterClick) {
                            Text("Daftar Toko")
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun HorizontalCardPlaceholder(
    storeName: String,
    address: String,
    price: String,
    status: String,
    onDetailClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = "Store icon",
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = storeName, style = MaterialTheme.typography.titleMedium)
                    when (status) {
                        "penuh" -> Text("Penuh", color = Color.Red, fontWeight = FontWeight.Bold)
                        "tutup" -> Text("Tutup", color = Color.Gray, fontWeight = FontWeight.Bold)
                        else -> Text("- 3 orang", color = Color.Gray)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = price)
                    Button(
                        onClick = onDetailClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cek Detail")
                    }
                }
                Text(text = address, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

// Preview 1: Home dengan data toko
@Preview(showBackground = true, showSystemUi = true, name = "Home - Normal")
@Composable
fun HomeContentWithThreePeoplePreview() {
    HomeContent(
        username = "Ananda",
        isLoading = false,
        stores = listOf(
            StoreItem(1, "Barber Ananda", "Jl. Merdeka", "Rp.10000", "tersedia")
        ),
        searchQuery = "",
        onSearchQueryChange = {},
        onProfileClick = {},
        onDetailClick = {},
        onLogoutClick = {},
        onRegisterClick = {},
        showLogoutDialog = false,
        onDismissLogoutDialog = {},
        onConfirmLogout = {},
        showRegisterConfirmation = false,
        onDismissRegisterConfirmation = {},
        onConfirmRegisterStore = {}
    )
}

// Preview 2: Toko penuh
@Preview(showBackground = true, showSystemUi = true, name = "Home - Penuh")
@Composable
fun HomeContentWithFullPreview() {
    HorizontalCardPlaceholder(
        storeName = "Barber Full",
        address = "Jl. Penuh",
        price = "Rp.15000",
        status = "penuh",
        onDetailClick = {}
    )
}

// Preview 3: Toko tutup
@Preview(showBackground = true, showSystemUi = true, name = "Home - Tutup")
@Composable
fun HomeContentWithClosedPreview() {
    HorizontalCardPlaceholder(
        storeName = "Barber Tutup",
        address = "Jl. Libur",
        price = "Rp.0",
        status = "tutup",
        onDetailClick = {}
    )
}