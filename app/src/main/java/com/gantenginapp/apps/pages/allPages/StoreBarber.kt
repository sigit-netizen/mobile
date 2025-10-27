package com.gantenginapp.apps.pages.allPages



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.CameraPosition
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.painterResource
import com.gantenginapp.apps.R

class BarberDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarberDetailScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarberDetailScreen() {
    var selectedTab by remember { mutableStateOf("Antrian") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Barber Wates") },
                navigationIcon = {
                    IconButton(onClick = { /* back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* info */ }) {
                        Icon(Icons.Default.Info, contentDescription = "Info")
                    }
                }
            )
        },
        bottomBar = {

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.gantengin),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )

                }
                Column {
                    Text("🟢 Buka", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                    Text("Gantengin Barbershop", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                    Text("19:00 - 23:00", color = Color.Gray)

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
                                if (selectedTab == tab) Color.Black else Color.Black,
                                RoundedCornerShape(12.dp)
                            )

                            .border(

                                width = if (selectedTab == tab) 2.dp else 1.dp,
                                color = if (selectedTab == tab) Color.Black else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )

                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(tab, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Konten Berdasarkan Tab
            when (selectedTab) {
                "Antrian" -> AntrianTable()
                "Style" -> StyleList()
                "Lokasi" -> LokasiMap()
            }
        }
    }
}

@Composable
fun AntrianTable() {
    val data = listOf(
        Triple("Akbar", "00:30", "Selesai"),
        Triple("Faiz", "01:00", "Proses"),
        Triple("Bagas", "01:30", "Antri"),
        Triple(first = "ananda", second = "02:00", third = "Antri"),
        Triple(first = "ananda", second = "02:00", third = "Antri"),
        Triple(first = "ananda", second = "02:00", third = "Antri"),
        Triple(first = "ananda", second = "02:00", third = "Antri"),
        Triple(first = "ananda", second = "02:00", third = "Antri"),
        Triple(first = "ananda", second = "02:00", third = "Antri"),
    )

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

        LazyColumn (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(data) { (nama, durasi, status) ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(nama, Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Black)
                    Text(durasi, Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Black)
                    Text(status, Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Black)
                }
            }
        }

        Button(
            onClick = { /* Booking logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Booking")
        }
    }
}

@Composable
fun StyleList() {
    val styles = listOf("Style 1", "Style 2", "Style 3", "Style 4", "Style 5", "Style 6")
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(styles) { style ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(Color(0xFFEFEFEF) ,RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = style,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                )

            }

        }

    }
}

@Composable
fun LokasiMap() {
    val jakarta = com.google.android.gms.maps.model.LatLng(-6.200000, 106.816666)
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



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BarberDetailScreenPreview() {
    BarberDetailScreen()
}

