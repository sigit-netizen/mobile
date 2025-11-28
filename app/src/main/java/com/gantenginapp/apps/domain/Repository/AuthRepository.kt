// File: app/src/main/java/com/gantenginapp/apps/domain/repository/AuthRepository.kt
package com.gantenginapp.apps.domain.Repository

import com.gantenginapp.apps.data.remote.dto.*

interface AuthRepository {
    suspend fun login(request: LoginRequest): UserResponse
    suspend fun register(request: RegisterRequest): UserResponse
    suspend fun getUserById(userId: String): UserResponse
}