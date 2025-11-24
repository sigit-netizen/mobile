// File: app/src/main/java/com/gantenginapp/apps/data/dto/LoginResponse.kt
package com.gantenginapp.apps.data.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val status: Boolean,
    @SerializedName("data")
    val user: User?,
    val message: String? = null,
    val token: String? = null
)