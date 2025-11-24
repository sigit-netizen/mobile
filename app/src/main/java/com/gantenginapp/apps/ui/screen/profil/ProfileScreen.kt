package com.gantenginapp.apps.ui.screen.profil

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import androidx.compose.foundation.background
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ProfileScreen(
    userId: Int,
    onBackClick: () -> Unit,
    onRegistStoreClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel() // ✅ Gunakan instance dari parameter
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Panggil fetch data saat userId berubah
    LaunchedEffect(userId) {
        viewModel.fetchUserProfile(userId)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Error: $error", color = Color.Red)
        }
        return
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                IconButton(onClick = { /* Info */ }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Profile Image
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = painterResource(id = R.drawable.gantengin),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
                IconButton(onClick = onEditProfileClick) {
                    Image(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Edit",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info boxes
            ProfileItem(icon = Icons.Default.Person, text = userProfile.username.takeIf { it.isNotEmpty() } ?: "-")
            ProfileItem(icon = Icons.Default.Phone, text = userProfile.phone.takeIf { it.isNotEmpty() } ?: "-")
            ProfileItem(icon = Icons.Default.Email, text = userProfile.email.takeIf { it.isNotEmpty() } ?: "-")
            ProfileItem(icon = Icons.Default.VerifiedUser, text = userProfile.role.takeIf { it.isNotEmpty() } ?: "-")
        }
    }
}

@Composable
fun ProfileItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        userId = 1,
        onBackClick = {},
        onRegistStoreClick = {},
        onEditProfileClick = {}
    )
}