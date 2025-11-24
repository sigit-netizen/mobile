package com.gantenginapp.apps.data.dto

import com.gantenginapp.apps.domain.model.User

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: User?
)