// File: app/src/main/java/com/gantenginapp/apps/data/repository/AuthRepositoryImpl.kt
package com.gantenginapp.apps.data.repository

import com.gantenginapp.apps.data.dto.*
import com.gantenginapp.apps.data.remote.ApiService
import com.gantenginapp.apps.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun login(request: LoginRequest): LoginResponse {
        return apiService.login(request)
    }

    override suspend fun register(request: RegisterRequest): RegisterResponse {
        return apiService.register(request)
    }

    override suspend fun getUserById(userId: Int): UserResponse {
        val response = apiService.getUserById(userId)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        }
        throw Exception("API call failed: ${response.code()}")
    }
}