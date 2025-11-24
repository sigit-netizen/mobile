package com.gantenginapp.apps.ui.screen.landingpages


import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.gantenginapp.apps.R
import  com.gantenginapp.apps.ui.theme.ColorCustom



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(username: String) {
    Scaffold(
        // Header
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.gantengin),
                                contentDescription = "App logo",
                                modifier = Modifier.size(50.dp)
                            )


                        }
                        Row {
                            TextButton(onClick = {}) {
                                Text("Market")
                            }
                            TextButton(onClick = {}) {
                                Text("Profile")
                            }

                        }
                    }
                }
            )
        },
        // Footer
        bottomBar = {
            BottomAppBar  (

            ){

                Text("Copyright © 2023 Gantengin App",
                )
            }
        },

        // Ngg tau ini apa
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle FAB click */ }) {
                Text("+")
            }

        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hello $username! Welcome to Gantengin App")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle button click */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorCustom.dark,
                    contentColor = Color.White
                )
            ) {
                Text("Mulai sekarang")
            }
        }

    }
}


// Main preview
@Preview(showBackground = true , showSystemUi = true)
@Composable
fun HomeContentPreview() {
    HomeContent(username = "Ananda")
}