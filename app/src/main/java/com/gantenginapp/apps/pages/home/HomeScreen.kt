package com.gantenginapp.apps.pages.home


import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.gantenginapp.apps.R
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.gantenginapp.apps.pages.allPages.ProfileActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    username: String,
    onProfileClick: () -> Unit,
    onDetailClick: () -> Unit

) {
    val context = LocalContext.current
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
                                 .border(1.dp, Color.Gray, CircleShape)
                                 .height(40.dp)
                         )


                        // Fitur  search
                         OutlinedTextField (
                             value = "",
                             onValueChange = {/* Handle value change */},
                             label = {Text(text = "Cari toko....")},
                             leadingIcon = {Icon(Icons.Default.Search, contentDescription = null)},
                             modifier = Modifier.width(250.dp),
                             shape = RoundedCornerShape(30.dp),
                             textStyle = LocalTextStyle.current.copy(fontSize = 10.sp),
                             singleLine = true
                         )


                         Box(
                             modifier = Modifier
                                 .size(30.dp)
                                 .clip(CircleShape)
                                 .background(Color.DarkGray.copy(alpha = 0.4f))
                                 .clickable {
                                    onProfileClick()
                                 },
                             contentAlignment = Alignment.Center
                         ) {
                             // Ini Iccon
                             Icon(

                                 imageVector = Icons.Default.Person,
                                 contentDescription = "Placeholder",
                                 tint = Color.White.copy(alpha = 0.6f),
                                 modifier = Modifier.size(60.dp)
                             )

                         }
                     }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                )
            )
        },
        // Footer
        bottomBar = {
            BottomAppBar  (
                modifier = Modifier.background(Color.White),
                containerColor = Color.White,
                contentColor = Color.Black,
                tonalElevation = 4.dp
            ){
                Text("Copyright © 2023 Gantengin App")
            }
        },

        // Ngg tau ini apa
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle FAB click */ }) {
                Text("+")
            }
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize().background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(20) {
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
        Row (
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
                // Ini Iccon
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Placeholder",
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(32.dp)
                )

            }
            // Text Disebelah kanan
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Row (
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
                Row (
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
                        Text(text = "Cek Detail")
                    }

                }


            }


        }

    }
}

// Main preview
@Preview(showBackground = true , showSystemUi = true)
@Composable
fun HomeContentPreview() {
    HomeContent(
        username = "Ananda",
        onProfileClick = {},
        onDetailClick = {}
        )
}