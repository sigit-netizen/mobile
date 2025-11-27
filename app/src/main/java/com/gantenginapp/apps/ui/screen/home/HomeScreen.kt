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
import androidx.compose.foundation.border
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import com.gantenginapp.apps.ui.screen.aiPage.AiPageViewModel
import com.gantenginapp.apps.ui.screen.aiPage.AiPageScreen
import androidx.activity.compose.BackHandler
import com.gantenginapp.apps.ui.theme.ColorCustom
import androidx.compose.foundation.text.BasicTextField

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

    val aiViewModel: AiPageViewModel = viewModel()

    val username by viewModel.username.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val stores by viewModel.filteredStores.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsState()

    HomeContent(
        viewModel = aiViewModel,

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
    viewModel: AiPageViewModel,

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
) {

    var isSearchActive by remember { mutableStateOf(false) }
    var selectedMenu by remember { mutableStateOf("ai") }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = onDismissLogoutDialog,
            title = { Text("Konfirmasi Logout") },
            text = { Text("Apakah Anda yakin ingin logout?") },
            confirmButton = {
                TextButton(onClick = onConfirmLogout) { Text("Ya") }
            },
            dismissButton = {
                TextButton(onClick = onDismissLogoutDialog) { Text("Tidak") }
            }
        )
    }
    BackHandler {
        if (selectedMenu != "home") {
            selectedMenu = "home"
        } else {

            onLogoutClick()
        }
    }



    if (showRegisterConfirmation) {
        AlertDialog(
            onDismissRequest = onDismissRegisterConfirmation,
            title = { Text("Konfirmasi Daftar Toko") },
            text = { Text("Anda belum memiliki toko. Apakah Anda ingin mendaftarkan toko?") },
            confirmButton = {
                TextButton(onClick = onConfirmRegisterStore) { Text("Ya") }
            },
            dismissButton = {
                TextButton(onClick = onDismissRegisterConfirmation) { Text("Tidak") }
            }
        )
    }
    Scaffold(
        containerColor = Color.White,
        topBar = {
            when(selectedMenu) {
                "home" -> {
                    TopAppBar(
                        modifier = Modifier.background(Color.White),

                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_toko),
                                    contentDescription = "App logo",
                                    modifier = Modifier
                                        .padding(start = 2.dp)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .clickable { onRegisterClick() }
                                )


                                Box(
                                    modifier = Modifier
                                        .width(200.dp)                         // 🔥 Lebar 200dp
                                        .height(30.dp)                         // Tinggi 30dp
                                        .border(1.dp, Color.Black, RoundedCornerShape(50.dp))   // 🔥 Border
                                        .background(Color.White, RoundedCornerShape(50.dp))     // 🔥 Background
                                        .padding(horizontal = 12.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        // ICON SEARCH
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier.size(16.dp) // menyesuaikan tinggi 30dp
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        // TEXTFIELD
                                        BasicTextField(
                                            value = searchQuery,
                                            onValueChange = {
                                                onSearchQueryChange(it)
                                                if (!isSearchActive && it.isNotBlank()) isSearchActive = true
                                            },
                                            singleLine = true,
                                            textStyle = LocalTextStyle.current.copy(
                                                fontSize = 13.sp,
                                                lineHeight = 16.sp,
                                                color = Color.Black
                                            ),
                                            decorationBox = { innerTextField ->
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.CenterStart
                                                ) {
                                                    if (searchQuery.isEmpty()) {
                                                        Text(
                                                            "Cari toko...",
                                                            fontSize = 13.sp,
                                                            color = Color.Gray
                                                        )
                                                    }
                                                    innerTextField()
                                                }
                                            }
                                            ,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }




                                Box(
                                    modifier = Modifier
                                        .padding(end = 15.dp)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .clickable { onProfileClick() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_profil),
                                        contentDescription = "Profile Picture",
                                        modifier = Modifier.size(30.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                    )

                }
                "ai" -> {
                    CenterAlignedTopAppBar(
                        title = { Text("StyleCut Ai", fontSize = 18.sp) },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.White,
                            titleContentColor = Color.Black,
                            navigationIconContentColor = Color.Black,
                            actionIconContentColor = Color.Black
                        ),
                        modifier = Modifier
                            .background(Color.White)
                            .statusBarsPadding(),

                    )
                }


            }

        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, contentColor = Color.Black) {
                NavigationBarItem(
                    selected = selectedMenu == "home",
                    onClick = { selectedMenu = "home" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedMenu == "ai",
                    onClick = { selectedMenu = "ai" },
                    icon = { Icon(Icons.Default.Adb, contentDescription = "AI") },
                    label = { Text("StyleCut Ai") }
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedMenu) {
                "home" -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().background(Color.White),
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

                "ai" -> {
                    AiPageScreen(
                        messages = viewModel.messages,
                        inputText = viewModel.inputText,
                        isAiThinking = viewModel.isAiThinking,
                        onInputChanged = viewModel::onInputChanged,
                        onSendClicked = viewModel::onSendClicked,
                        onMessageLongPress = { /* optional */ }
                    )
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.DarkGray.copy(alpha = 0.4f)), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.Store, contentDescription = "Store icon", tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(32.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = storeName, style = MaterialTheme.typography.titleMedium)
                    when (status) {
                        "penuh" -> Text("Penuh", color = Color.Red, fontWeight = FontWeight.Bold)
                        "tutup" -> Text("Tutup", color = Color.Gray, fontWeight = FontWeight.Bold)
                        else -> Text("- 3 orang", color = Color.Gray)
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = price)
                    Button(onClick = onDetailClick, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray, contentColor = Color.White)) {
                        Text("Cek Detail")
                    }
                }

                Text(text = address, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val dataDumy = listOf(
        StoreItem(
            id = 1,
            name = "Barber Premium",
            address = "Jl. Sudirman No. 10",
            price = "Rp 25.000",
            status = "penuh"
        ),
        StoreItem(
            id = 2,
            name = "Gentlemen Cut",
            address = "Jl. Merdeka No. 20",
            price = "Rp 30.000",
            status = "buka"
        )
    )

    val aiViewModel: AiPageViewModel = viewModel()
    HomeContent(
        viewModel = aiViewModel,      // dummy VM
        isLoading = false,
        stores = dataDumy,
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
