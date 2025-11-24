package com.gantenginapp.apps.domain.model

data class User(
    val id: String? = null,
    val username: String,
    val email: String,
    val noHp: String,
    val role: String = "user"
)