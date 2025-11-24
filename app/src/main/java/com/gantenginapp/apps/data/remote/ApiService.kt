// File: app/src/main/java/com/gantenginapp/apps/data/remote/ApiService.kt
package com.gantenginapp.apps.data.remote

import com.gantenginapp.apps.data.dto.LoginRequest
import com.gantenginapp.apps.data.dto.LoginResponse
import com.gantenginapp.apps.data.dto.RegisterRequest
import com.gantenginapp.apps.data.dto.RegisterResponse
import com.gantenginapp.apps.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Di ApiService
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("auth/profile/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<UserResponse>
}