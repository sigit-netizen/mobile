package com.gantenginapp.apps.ui.screen.aiPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.gantenginapp.apps.ui.theme.LearnAndroidDasarTheme

class AiPageActivity : ComponentActivity() {

    private val viewModel: AiPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LearnAndroidDasarTheme {
                AiPageScreen(
                    messages = viewModel.messages,
                    inputText = viewModel.inputText,
                    isAiThinking = viewModel.isAiThinking,
                    onInputChanged = viewModel::onInputChanged,
                    onSendClicked = viewModel::onSendClicked,
                    onMessageLongPress = {  }
                )
            }
        }
    }
}
