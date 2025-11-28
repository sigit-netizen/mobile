// File: app/src/main/java/com/gantenginapp/apps/data/dto/User.kt
package com.gantenginapp.apps.data.remote.dto

data class User(
    val id: Int,
    val username: String,
    val role: String,
    val email: String,
    val noHP: String
)