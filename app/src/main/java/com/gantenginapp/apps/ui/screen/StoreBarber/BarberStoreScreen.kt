package com.gantenginapp.apps.ui.screen.StoreBarber

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.gantenginapp.apps.domain.model.Antrian
import com.gantenginapp.apps.ui.theme.ColorCustom
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarberDetailScreen(
    viewModel: BarberStoreViewModel,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Antrian") }

    // Mengambil data store dari view model bray
    val store by viewModel.store.collectAsState()
    val antrianList by viewModel.antrian.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDataStore()
    }
    val isLoading by viewModel.isRefreshingLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message!!, withDismissAction = true)
            viewModel.clearMessage()
        }
    }
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.loadDataStore() },
        indicatorPadding = PaddingValues(top = 28.dp)
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("${store.storeName}") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* info */ }) {
                            Icon(Icons.Default.Info, contentDescription = "Info")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ColorCustom.bg,
                        titleContentColor = Color.Black,
                        navigationIconContentColor = Color.Black,
                        actionIconContentColor = Color.Black
                    )
                )
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.Start,

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gantengin),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(30.dp))


                    Column (
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(if (store.status == 1)"🟢 Buka" else "🔴 Tutup", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                        Text("${store.storeName}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                        Text("${store.openingHours} - ${store.closingTime}", color = Color.Gray)
                        Text("Rp.${store.price ?: "Belum diset"}", color = Color.Gray)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Antrian", "Style", "Lokasi").forEach { tab ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = tab }
                                .background(
                                    if (selectedTab == tab) Color.Black else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (selectedTab == tab) Color.Black else Color.LightGray,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tab, fontWeight = FontWeight.Bold, color = if (selectedTab == tab) Color.White else Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTab) {
                    "Antrian" -> AntrianTable(antrianList, viewModel)
                    "Style" -> StyleList()
                    "Lokasi" -> LokasiMap()
                }
            }

        }

    }

}

@Composable

fun AntrianTable(
    listAntrian: List<Antrian>,
    viewModel: BarberStoreViewModel
) {
    fun getCurrentTimeString(): String {
        val cal = java.util.Calendar.getInstance()
        val hour = cal.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = cal.get(java.util.Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }

    var showDialog by remember { mutableStateOf(false) }
    val filteredAntrian = listAntrian.filter { item ->
        (item.status == 0 || item.status == 1) &&
                item.waktu > getCurrentTimeString()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0))
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Nama", Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = Color.Black)
                Text("Durasi", Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = Color.Black)
                Text("Status", Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = Color.Black)
            }

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (filteredAntrian.isEmpty()) {
                    item {
                        Text("belum ada antrian ", modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), textAlign = TextAlign.Center, color = Color.Gray)

                    }
                } else {
                    items(filteredAntrian) { item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.customerName, Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Black)
                            Text(item.waktu, Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Black)

                            // Mapping status
                            val statusText = when (item.status) {
                                0 -> "Kosong"
                                1 -> "Terisi"
                                2 -> "Selesai"
                                else -> "Unknown"
                            }

                            val statusColor = when (item.status) {
                                0 -> Color.Gray
                                1 -> Color(0xFFFFA000) // Kuning proses
                                2 -> Color(0xFF4CAF50) // Hijau selesai
                                else -> Color.Black
                            }
                            Text(
                                text = statusText,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                color = statusColor
                            )
                        }
                    }


                }
            }
        }
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Booking")
        }

        if (showDialog) {
            BookingDialog(
                onDismiss = { showDialog = false },
                onSubmit = { name, phone ,slot->
                    viewModel.postAntrian(
                        customerName = name,
                        noHp = phone,
                        slot = slot
                    )
                    showDialog = false
                },
                listAntrian = listAntrian
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDialog(
    listAntrian: List<Antrian>,
    onDismiss: () -> Unit,
    onSubmit: (String, String, Antrian) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    fun getCurrentTimeString(): String {
        val cal = java.util.Calendar.getInstance()
        val hour = cal.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = cal.get(java.util.Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }

    val kosongSlots = listAntrian.filter { slot ->
        slot.status == 0 && slot.waktu > getCurrentTimeString()
    }


    var selectedSlot by remember { mutableStateOf<Antrian?>(null) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var slotError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Form Booking") },
        text = {
            Column {

                // Nama
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Pelanggan") },
                    isError = nameError != null
                )
                if (nameError != null) Text(nameError!!, color = Color.Red, fontSize = 12.sp)

                Spacer(Modifier.height(8.dp))

                // Nomor HP
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Nomor HP") },
                    isError = phoneError != null
                )
                if (phoneError != null) Text(phoneError!!, color = Color.Red, fontSize = 12.sp)

                Spacer(Modifier.height(8.dp))


                // 🔥 Dropdown FIXED
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedSlot?.waktu ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Pilih Waktu Antrian") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        isError = slotError != null
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        kosongSlots.forEach { slot ->
                            DropdownMenuItem(
                                text = { Text(slot.waktu) },
                                onClick = {
                                    selectedSlot = slot
                                    expanded = false
                                    slotError = null
                                }
                            )
                        }
                    }
                }

                if (slotError != null) Text(slotError!!, color = Color.Red, fontSize = 12.sp)
            }
        },
        confirmButton = {
            Button(onClick = {
                var valid = true

                if (name.isBlank()) {
                    nameError = "Nama tidak boleh kosong"
                    valid = false
                }
                if (phone.isBlank()) {
                    phoneError = "Nomor HP tidak boleh kosong"
                    valid = false
                }
                if (selectedSlot == null) {
                    slotError = "Pilih slot waktu!"
                    valid = false
                }

                if (valid) {
                    onSubmit(name, phone, selectedSlot!!)
                }

            }) { Text("Ngantri") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
fun StyleList() {
    val styles = listOf("Style 1", "Style 2", "Style 3", "Style 4", "Style 5", "Style 6")
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(styles) { style ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = style,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun LokasiMap() {
    val jakarta = LatLng(-6.200000, 106.816666)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(jakarta, 14f)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp)),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = jakarta),
                title = "Barber Wates",
                snippet = "Jl. Contoh No.123, Wates"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Alamat: Jl. Contoh No.123, Wates, Yogyakarta",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
// ✅ Preview untuk seluruh screen
@Preview(showBackground = true, showSystemUi = true, name = "Barber Detail - Full Screen")
@Composable
fun BarberDetailScreenPreview() {
    BarberDetailScreen(
        viewModel(),
        onBackClick = { /* Tidak melakukan apa-apa di preview */ })
}

// ✅ Preview untuk tab Antrian
@Preview(showBackground = true, showSystemUi = true, name = "Barber Detail - Tab Antrian")
@Composable
fun AntrianTablePreview() {
    val dummyList = listOf(
        Antrian(
            idAntrian = 1,
            idStore = 1,
            customerName = "Akbar",
            noHp = "081234567890",
            waktu = "00:30",
            status = 0
        ),
        Antrian(
            idAntrian = 2,
            idStore = 1,
            customerName = "Faiz",
            noHp = "081234567891",
            waktu = "01:00",
            status = 1
        )
    )

    AntrianTable(dummyList, viewModel())
}

// ✅ Preview untuk tab Style
@Preview(showBackground = true, showSystemUi = true, name = "Barber Detail - Tab Style")
@Composable
fun StyleListPreview() {
    StyleList()
}

// ✅ Preview untuk tab Lokasi
@Preview(showBackground = true, showSystemUi = true, name = "Barber Detail - Tab Lokasi")
@Composable
fun LokasiMapPreview() {
    LokasiMap()
}