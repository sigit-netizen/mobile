// File: app/src/main/java/com/gantenginapp/apps/data/dto/UserResponse.kt
package com.gantenginapp.apps.data.remote.dto

import com.google.gson.annotations.SerializedName // Tambahkan import ini

// Bisa jadi base class bersama
data class UserResponse(
    val status: Boolean,
    @SerializedName("data")
    val user: User?,
    val message: String? = null,
    val token: String? = null // hanya login yang punya
)