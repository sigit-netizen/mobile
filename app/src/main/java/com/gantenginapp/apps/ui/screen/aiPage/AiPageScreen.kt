package com.gantenginapp.apps.ui.screen.aiPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import com.gantenginapp.apps.ui.theme.ColorCustom
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPageScreen(
    messages: List<Message>,
    inputText: androidx.compose.runtime.State<String>,
    isAiThinking: androidx.compose.runtime.State<Boolean>,
    onInputChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
    onMessageLongPress: (Message) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.background(ColorCustom.bg),
        bottomBar = {
            Column (
                modifier = Modifier.background(ColorCustom.bg).windowInsetsPadding(WindowInsets(0))
            ) {
                if (isAiThinking.value) {
                    // simple typing indicator
                    Text(
                        "AI is typing...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(6.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(ColorCustom.bg),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText.value,
                        onValueChange = onInputChanged,
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 56.dp)
                            .background(ColorCustom.bg),
                        placeholder = { Text("Tanyakan tentang Model...") },
                        maxLines = 4,
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FloatingActionButton(
                        onClick = {
                            onSendClicked()
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.size(48.dp),
                        containerColor = ColorCustom.black,
                        contentColor = ColorCustom.bg
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            }
        }
    ) { innerPadding ->
        ChatList(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(ColorCustom.bg) ,
            messages = messages,
            onMessageLongPress = onMessageLongPress
        )
    }
}

@Composable
private fun ChatList(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    onMessageLongPress: (Message) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp).background(ColorCustom.bg),
        reverseLayout = false
    ) {
        itemsIndexed(messages) { index, msg ->
            val textColor =  MaterialTheme.colorScheme.onSurfaceVariant

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(max = 270.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ColorCustom.gray)
                        .clickable { /* optional click */ }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = msg.text,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }


        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AiPageScreenPreview() {
    val sampleMessages = listOf(
        Message(text = "Halo bro!", isUser = true),
        Message(text = "Halo! Aku Hair Advisor AI. Ada yang bisa dibantu?", isUser = false),
        Message(text = "Rekomendasi rambut buat wajah oval.", isUser = true),
        Message(text = "Wajah oval cocok dengan hampir semua gaya rambut bro!", isUser = false)
    )

    val inputState = remember { mutableStateOf("") }
    val thinkingState = remember { mutableStateOf(false) }

    MaterialTheme {
        AiPageScreen(
            messages = sampleMessages,
            inputText = inputState,
            isAiThinking = thinkingState,
            onInputChanged = { inputState.value = it },
            onSendClicked = {},
            onMessageLongPress = {}
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AiPageScreenPreviewWrapper() {
    val sampleMessages = listOf(
        Message("Halo bro!", "hallo", isUser = true),
        Message("Halo! Aku Hair Advisor AI. Ada yang bisa dibantu?", "false", isUser = false)
    )

    MaterialTheme {
        AiPageScreen(
            messages = sampleMessages,
            inputText = remember { mutableStateOf("") },
            isAiThinking = remember { mutableStateOf(false) },
            onInputChanged = {},
            onSendClicked = {},
            onMessageLongPress = {}
        )
    }
}

