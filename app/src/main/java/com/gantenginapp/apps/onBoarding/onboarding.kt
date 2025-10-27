package com.gantenginapp.apps.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.ExperimentalFoundationApi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LandingScreen(
    onSkipClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val pages = listOf(
        "Image 1" to "Temukan berbagai fitur menarik untuk mempermudah harimu.",
        "Image 2" to "Nikmati pengalaman cepat dan ringan dengan desain modern.",
        "Image 3" to "Mulai perjalananmu bersama kami hari ini!"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Pager (geser-geser 3 card)
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // Gambar Sementara
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pages[page].first,
                            fontSize = 22.sp,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Deskripsi
                    Text(
                        text = pages[page].second,
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Indikator dots
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 12.dp else 8.dp)
                            .background(
                                if (isSelected) Color(0xFF6200EE) else Color.Gray,
                                shape = MaterialTheme.shapes.small
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Tombol Skip
            Button(
                onClick = onSkipClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text(
                    text = "Skip",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewLandingScreen() {
    LandingScreen(
        onSkipClick = {}
    )
}
