package com.gantenginapp.apps.ui.screen.adminstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gantenginapp.apps.domain.model.Antrian
import com.gantenginapp.apps.ui.screen.StoreBarber.AntrianTable
import com.gantenginapp.apps.data.remote.dto.StoreUpdateRequest
import com.gantenginapp.apps.domain.model.Store
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminStoreScreen(
    viewModel: AdminStoreViewModel,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Antrian") }
    val store by viewModel.store.collectAsState()
    val antrianList by viewModel.antrian.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var showGenerateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDataAdminStore()
    }
    val isLoading by viewModel.isRefreshingLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    val message by viewModel.message.collectAsState()

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {viewModel.loadDataAdminStore()},
        indicatorPadding = PaddingValues(top = 28.dp)
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(store?.storeName ?: "Belum Diedit") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) { //
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Info, contentDescription = "Info")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,        // background putih
                        titleContentColor = Color.Black,     // warna title
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
                    .background(Color.White)
            ) {
                // Status Barber
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // FOTO TOKO
                    Image(
                        painter = painterResource(id = R.drawable.gantengin),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(24.dp)) // Jarak dari gambar biar nggak mepet

                    // INFO + TOMBOL
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start // Center kiri-kanan
                    ) {
                        Text(
                            when (store?.status) {
                                1 -> "🟢 Buka"
                                else -> "🔴 Tutup"
                            },
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            store?.storeName ?: "Belum diedit",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )

                        Text(
                            "${store?.openingHours ?: "00:00"} - ${store?.closingTime ?: "00:00"}",
                            color = Color.Gray
                        )
                        Text(
                            "Rp.${store?.price ?: "0"}",
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(12.dp))

                        // BUTTON DALAM FLEX ROW
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start, // Center secara horizontal
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showGenerateDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                modifier = Modifier.height(38.dp) // kecil & rapi
                            ) {
                                Text("Generate", color = Color.White, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Button(
                                onClick = { isEditing = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                modifier = Modifier.height(38.dp)
                            ) {
                                Text("Edit", color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                        if (showGenerateDialog) {
                            AlertDialog(
                                onDismissRequest = { showGenerateDialog = false },
                                title = { Text("Generate Antrian") },
                                text = {
                                    Text(
                                        "Fitur ini akan membuat slot antrian otomatis berdasarkan jam buka, jam tutup, dan durasi per pelanggan.\n\n" +
                                                "Pastikan data toko sudah benar sebelum melanjutkan."
                                    )
                                },
                                confirmButton = {
                                    Button(onClick = {
                                        showGenerateDialog = false
                                        store?.idStore?.let { id ->
                                            viewModel.generateAntrian(id)
                                        }
                                    }) {
                                        Text("Konfirmasi")
                                    }
                                },
                                dismissButton = {
                                    OutlinedButton(onClick = { showGenerateDialog = false }) {
                                        Text("Batal")
                                    }
                                }
                            )
                        }


                        if (isEditing && store != null) {
                            AlertDialog(
                                onDismissRequest = { isEditing = false },
                                title = { Text("Edit Data Toko") },
                                text = {
                                    EditStoreFormPopup(
                                        store = store!!,
                                        onCancel = { isEditing = false },
                                        onSave = { updated ->
                                            viewModel.updateStore(updated)
                                            isEditing = false
                                        }
                                    )
                                },
                                confirmButton = {},
                                dismissButton = {}
                            )
                        }


                    }


                // Tabs
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

                // Konten Berdasarkan Tab
                when (selectedTab) {
                    "Antrian" -> AdminAntrianTable(antrianList, viewModel)
                    "Style" -> AdminStyleList()
                    "Lokasi" -> AdminLokasiMap()
                }
            }
        }
    }

}


@Composable
fun AdminAntrianTable(
    listAntrian: List<Antrian>,
    viewModel: AdminStoreViewModel
) {
    val message by viewModel.message.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // Kalau kosong → tampilkan message
    if (listAntrian.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = message ?: "Belum ada antrian",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
        return
    }


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
            Text("Nama", Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Text("Durasi", Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Text("Status", Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }

        LazyColumn {
            items(listAntrian) { item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.customerName ?: "", Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text(item.waktu ?: "-", Modifier.weight(1f), textAlign = TextAlign.Center)
                    if (item.status != 0 ) {
                        Box(
                            Modifier.weight(1f), contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)                         // biar kecil
                                    .border(2.dp, Color.Gray, RoundedCornerShape(6.dp))
                                    .clickable { showDialog = true }     // klik = tampil dialog
                                    .padding(4.dp),                      // ruang dalam
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selesai",
                                    tint = if (item.status == 2) Color.Green else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                             }

                        }

                        // === DIALOG KONFIRMASI ===
                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text("Konfirmasi") },
                                text = { Text("Yakin bookingan ini sudah selesai?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        viewModel.deleteAntrian(item.idAntrian)
                                        showDialog = false
                                    }) {
                                        Text("Ya, Selesai")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDialog = false }) {
                                        Text("Batal")
                                    }
                                }
                            )
                        }
                        } else {
                        Text(
                            when (item.status ?: 0) {
                                0 -> "Kosong"
                                1 -> "Terisi"
                                else -> "Selesai"
                            },
                            Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun AdminStyleList() {
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
fun AdminLokasiMap() {
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

// ✅ Preview untuk tab Antrian
@Preview(showBackground = true, showSystemUi = true, name = "Barber Detail - Tab Antrian")
@Composable
fun AdminAntrianTablePreview() {
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
fun AdminStyleListPreview() {
    AdminStyleList()
}

// ✅ Preview untuk tab Lokasi
@Preview(showBackground = true, showSystemUi = true, name = "Barber Detail - Tab Lokasi")
@Composable
fun AdminLokasiMapPreview() {
    AdminLokasiMap()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStoreFormPopup(
    store: Store,
    onCancel: () -> Unit,
    onSave: (StoreUpdateRequest) -> Unit
) {
    var name by remember { mutableStateOf(store.storeName ?: "") }
    var price by remember { mutableStateOf(store.price?.toString() ?: "") }
    var alamat by remember { mutableStateOf(store.alamat ?: "") }
    var open by remember { mutableStateOf(store.openingHours ?: "") }
    var close by remember { mutableStateOf(store.closingTime ?: "") }
    var durasi by remember { mutableStateOf(store.durasi?.toString() ?: "") }
    val filterdStatus = if(store.status == 0)  "tutup" else "buka"

    var expanded by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf(filterdStatus) }
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Toko") })
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Harga") })
        OutlinedTextField(value = alamat, onValueChange = { alamat = it }, label = { Text("Alamat") })
        OutlinedTextField(value = open, onValueChange = { open = it }, label = { Text("Jam Buka (HH:MM:SS)") })
        OutlinedTextField(value = close, onValueChange = { close = it }, label = { Text("Jam Tutup (HH:MM:SS)") })
        OutlinedTextField(value = durasi, onValueChange = { durasi = it }, label = { Text("Durasi (menit)") })
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = status,
                onValueChange = {},
                readOnly = true,
                label = { Text("Status Toko") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("buka") },
                    onClick = {
                        status = "buka"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("tutup") },
                    onClick = {
                        status = "tutup"
                        expanded = false
                    }
                )
            }
        }


        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onCancel) {
                Text("Batal")
            }
            Button(onClick = {
                onSave(
                    StoreUpdateRequest(
                        storeName = name,
                        price = price.toIntOrNull() ?: 0,
                        alamat = alamat,
                        openingHours = open,
                        closingTime = close,
                        durasi = durasi.toIntOrNull() ?: 0,
                        status = if (status == "buka") 1 else 0
                    )
                )
            }) {
                Text("Simpan")
            }
        }
    }
}

