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


    val messages = mutableStateListOf<Message>()
    val inputText = mutableStateOf("")
    val isAiThinking = mutableStateOf(false)

    init {

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


        val userMsg = Message(text = text, isUser = true)
        messages.add(userMsg)
        inputText.value = ""


        simulateAiResponse(userMsg.text)
    }

    private fun simulateAiResponse(userText: String) {
        viewModelScope.launch {
            isAiThinking.value = true

            delay(700)

            
            val response = buildMockResponse(userText)

            messages.add(Message(text = response, isUser = false))
            isAiThinking.value = false
        }
    }

    private fun buildMockResponse(input: String): String {
        val normalized = input.lowercase()

        if (normalized.contains("makasih") ||
            normalized.contains("terima kasih") ||
            normalized.contains("trimakasih") ||
            normalized.contains("thanks") ||
            normalized.contains("thank you")) {

            return "Sama-sama! Kalau ada yang mau ditanya lagi soal gaya rambut atau style lain, tinggal bilang aja ya 😄✂️"
        }



        val faceShape = when {
            "oval" in normalized -> "oval"
            "bulat" in normalized -> "bulat"
            "round" in normalized -> "bulat"
            "kotak" in normalized || "square" in normalized -> "kotak"
            "heart" in normalized || "hati" in normalized -> "heart"
            "diamond" in normalized -> "diamond"
            "oblong" in normalized || "panjang" in normalized -> "oblong"
            else -> null
        }


        val hairType = when {
            "lurus" in normalized || "straight" in normalized -> "lurus"
            "ikal" in normalized || "wavy" in normalized -> "ikal"
            "keriting" in normalized || "curly" in normalized -> "keriting"
            else -> null
        }


        val density = when {
            "tipis" in normalized -> "tipis"
            "tebal" in normalized -> "tebal"
            else -> null
        }


        if (faceShape != null && hairType != null) {
            return specificRecommendation(faceShape, hairType, density)
        }


        if (faceShape != null) {
            return "Oke, wajah kamu bentuk **$faceShape**.\nSekarang jelasin jenis rambutmu: Lurus / Ikal / Keriting?"
        }

        if (hairType != null) {
            return "Noted, rambutmu **$hairType**.\nBentuk wajahmu apa? Oval / Bulat / Kotak / Heart / Diamond / Oblong?"
        }


        return "Menangkap: \"$input\".\nKamu bisa mulai dengan menjelaskan bentuk wajah atau jenis rambutmu."
    }

    private fun specificRecommendation(
        face: String,
        hair: String,
        density: String?
    ): String {

        return when (face to hair) {


            "oval" to "lurus" -> """
            Untuk wajah **oval** + rambut **lurus**, gaya terbaik adalah:

            ⭐ **Comma Hair (Korean Style)**  
            - Cocok banget karena framing wajahnya pas  
            - Memberi volume di depan  
            - Sangat aman untuk rambut lurus

            Alternatif:
            • Middle Part Classic  
            • Layered Undercut  
            • Soft Two Block

            Mau aku kasih referensi gambar/style?
        """.trimIndent()

            "oval" to "ikal" -> """
            Wajah **oval** + rambut **ikal** cocok banget ke:

            ⭐ **Messy Wavy Crop**  
            - Kelihatan natural & modern  
            - Bikin tekstur ikalnya muncul

            Alternatif:
            • Curly Two Block  
            • Wavy Fringe  
            • Soft Perm Korean Look
        """.trimIndent()


            "bulat" to "lurus" -> """
            Wajah **bulat** + rambut **lurus** cocok:

            ⭐ **Textured Quiff**  
            - Bikin wajah terlihat lebih panjang  
            - Ada volume di atas

            Alternatif:
            • Short Pompadour  
            • Messy Undercut  
            • Side Swept Classic
        """.trimIndent()

            "bulat" to "ikal" -> """
            Wajah **bulat** + rambut **ikal** paling cocok:

            ⭐ **Textured Crop + Mid Fade**  
            - Membentuk definisi di samping  
            - Bikin ikal lebih rapi & proporsional
        """.trimIndent()


            "kotak" to "lurus" -> """
            Wajah **kotak** + rambut **lurus** cocok:

            ⭐ **Side Part Clean Cut**  
            - Menyeimbangkan garis rahang yang tegas  

            Alternatif:
            • Ivy League  
            • Soft Fade Crop  
            • Slick Back Modern
        """.trimIndent()


            else -> """
            Dari kombinasi yang kamu kasih (wajah **$face**, rambut **$hair**), aku bisa rekomendasikan:

            ⭐ **Textured Crop / Two Block / Side Part Modern**
            (Detail lebih spesifik bisa aku kasih kalau kamu jelasin rambutmu tebal atau tipis)
        """.trimIndent()
        }
    }




    fun deleteMessage(id: String) {
        messages.removeAll { it.id == id }
    }
}
