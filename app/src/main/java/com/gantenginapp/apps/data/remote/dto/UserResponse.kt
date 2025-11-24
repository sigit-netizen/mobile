// File: app/src/main/java/com/gantenginapp/apps/data/dto/UserResponse.kt
package com.gantenginapp.apps.data.dto

import com.google.gson.annotations.SerializedName // Tambahkan import ini

data class UserResponse(
    val status: Boolean,
    @SerializedName("data") // <-- Tambahkan ini agar 'data' di JSON dibaca sebagai 'user'
    val user: User?,
    val message: String? = null
)