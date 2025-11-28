// app/src/main/java/com/gantenginapp/apps/data/dto/UpdateUserRequest.kt
package com.gantenginapp.apps.data.remote.dto

data class UpdateUserRequest(
    val username: String? = null,
    val noHP: String? = null,      // ✅ Sesuai backend: "noHP"
    val email: String? = null,
    val password: String? = null
)