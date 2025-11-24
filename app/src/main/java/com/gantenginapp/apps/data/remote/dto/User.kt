// File: app/src/main/java/com/gantenginapp/apps/data/dto/User.kt
package com.gantenginapp.apps.data.dto

data class User(
    val id: Int,
    val username: String,
    // Jangan sertakan password di sini, karena tidak dikirim dari API
    val role: String,
    val email: String,
    val noHP: String
    // Tambahkan field lain jika ada di tabel user
)