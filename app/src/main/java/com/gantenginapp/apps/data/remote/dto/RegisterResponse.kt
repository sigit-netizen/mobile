package com.gantenginapp.apps.data.dto

import com.gantenginapp.apps.domain.model.User

data class RegisterResponse(
    val message: String,
    val status: Boolean,
    val data: User?
)