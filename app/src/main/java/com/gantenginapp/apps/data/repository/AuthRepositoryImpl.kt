// app/src/main/java/com/gantenginapp/apps/data/repository/AuthRepositoryImpl.kt
package com.gantenginapp.apps.data.repository

import com.gantenginapp.apps.data.dto.*
import com.gantenginapp.apps.data.remote.ApiService

class AuthRepositoryImpl(
    private val apiService: ApiService
) {
    suspend fun login(request: LoginRequest): UserResponse {
        return apiService.login(request)
    }

    suspend fun register(request: RegisterRequest): UserResponse {
        return apiService.register(request)
    }
    suspend fun getUserById(userId: Int): UserResponse {
        return apiService.getUserById(userId)
    }

    suspend fun updateUser(
        userId: Int,
        username: String,
        noHp : String,       // di sini kita pakai "phone" (natural di Kotlin)
        email: String,
        password: String? = null
    ): ApiResponse {
        val request = UpdateUserRequest(
            username = username,
            noHP = noHp,        // ✅ map "phone" → "noHP" untuk API
            email = email,
            password = password
        )
        return apiService.updateUser(userId, request)
    }

    suspend fun deleteUser(userId: Int): ApiResponse {
        return apiService.deleteUser(userId)
    }
}