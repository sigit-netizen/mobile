
package com.gantenginapp.apps.data.repository

import com.gantenginapp.apps.data.remote.dto.*
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
        noHp : String,
        email: String,
        password: String? = null
    ): ApiResponse {
        val request = UpdateUserRequest(
            username = username,
            noHP = noHp,
            email = email,
            password = password
        )
        return apiService.updateUser(userId, request)
    }

    suspend fun deleteUser(userId: Int): ApiResponse {
        return apiService.deleteUser(userId)
    }
}