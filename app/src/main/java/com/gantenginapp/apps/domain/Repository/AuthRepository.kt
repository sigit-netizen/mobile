// File: app/src/main/java/com/gantenginapp/apps/domain/repository/AuthRepository.kt
package com.gantenginapp.apps.domain.repository

import com.gantenginapp.apps.data.dto.LoginRequest
import com.gantenginapp.apps.data.dto.RegisterRequest
import com.gantenginapp.apps.data.dto.UserResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): UserResponse
    suspend fun register(request: RegisterRequest): UserResponse
    suspend fun getUserById(userId: Int): UserResponse
}