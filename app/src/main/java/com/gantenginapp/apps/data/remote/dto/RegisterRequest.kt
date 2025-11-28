package com.gantenginapp.apps.data.remote.dto

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val noHp: String,
    val role: String? = "user"
)