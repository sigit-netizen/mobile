package com.gantenginapp.apps.ui.screen.aiPage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class AiPageViewModel : ViewModel() {

    // public read-only lists for UI
    val messages = mutableStateListOf<Message>()
    val inputText = mutableStateOf("")
    val isAiThinking = mutableStateOf(false)

    init {
        // optional welcome message
        messages.add(
            Message(
                text = "Halo! Aku siap bantu rekomendasi gaya rambut. Coba ketik: \"Halo\" atau \"Mulai\"",
                isUser = false
            )
        )
    }

    fun onInputChanged(new: String) {
        inputText.value = new
    }

    fun onSendClicked() {
        val text = inputText.value.trim()
        if (text.isEmpty()) return

        // append user message
        val userMsg = Message(text = text, isUser = true)
        messages.add(userMsg)
        inputText.value = ""

        // simulate AI thinking + response (replace with real API call)
        simulateAiResponse(userMsg.text)
    }

    private fun simulateAiResponse(userText: String) {
        viewModelScope.launch {
            isAiThinking.value = true
            // small delay to show typing indicator
            delay(700)

            // Basic mock "AI" logic: (you can replace with more advanced logic)
            val response = buildMockResponse(userText)

            messages.add(Message(text = response, isUser = false))
            isAiThinking.value = false
        }
    }

    private fun buildMockResponse(input: String): String {
        // Very simple heuristics — replace with actual model/API integration.
        val normalized = input.lowercase()
        return when {
            "oval" in normalized -> "Oke, kamu bilang bentuk wajah oval — itu fleksibel, banyak gaya cocok..."
            "lurus" in normalized -> "Rambut lurus cocok dengan gaya textured crop, quiff, atau messy spiky..."
            "tipis" in normalized -> "Rambut tipis butuh tekstur dan layering. Pakai clay atau powder untuk menambah volume."
            "short" in normalized || "pendek" in normalized -> "Gaya pendek yang cocok: textured crop + taper, short quiff..."
            "mulai" in normalized || "halo" in normalized -> "Siap! Jelasin bentuk wajahmu (Oval/Bulat/Kotak/Heart/Diamond/Oblong)."
            else -> "Menangkap: \"$input\". Mau aku rekomendasi gaya berdasarkan: bentuk wajah, jenis rambut, ketebalan, panjang, atau preferensi styling?"
        }
    }

    // optional helpers: delete message, edit, etc.
    fun deleteMessage(id: String) {
        messages.removeAll { it.id == id }
    }
}
