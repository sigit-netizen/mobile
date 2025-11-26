package com.gantenginapp.apps.ui.screen.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.CircleShape
import com.gantenginapp.apps.R

data class OnboardingPage(
    val imageResId: Int,
    val description: String
)

val defaultOnboardingPages = listOf(
    OnboardingPage(R.drawable.img1, "Temukan berbagai fitur menarik untuk mempermudah harimu."),
    OnboardingPage(R.drawable.img2, "Nikmati pengalaman cepat dan ringan dengan desain modern."),
    OnboardingPage(R.drawable.img3, "Mulai perjalananmu bersama kami hari ini!")
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LandingScreen(
    onSkipClick: () -> Unit,
    pages: List<OnboardingPage> = defaultOnboardingPages
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            // 🖼️ Gambar + teks — pakai ukuran tetap agar tidak memenuhi layar
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .height(550.dp) // ⬅️ Tetapkan tinggi maksimal
                    .fillMaxWidth()
            ) { page ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Image(
                        painter = painterResource(id = pages[page].imageResId),
                        contentDescription = null,
                        modifier = Modifier.size(400.dp), // ⬇️ Ukuran lebih kecil agar tidak terlalu besar
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = pages[page].description,
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }

            // ⬇️ JARAK DIPERKECIL — DOTS DAN TOMBOL DI ATASKAN!
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 12.dp else 8.dp)
                            .background(
                                color = if (isSelected) Color(0xFF6200EE) else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // ✅ TOMBOL SKIP — DI ATASKAN LEBIH TINGGI
            Button(
                onClick = onSkipClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 5.dp), // ⬇️ Kurangi padding bottom
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text(
                    text = "Skip",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // ⬆️ Tambahkan spacer kecil di bawah — agar tidak menyentuh tepi bawah
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewLandingScreen() {
    LandingScreen(onSkipClick = {})
}