// app/src/main/java/com/gantenginapp/apps/data/remote/ApiService.kt
package com.gantenginapp.apps.data.remote

import com.gantenginapp.apps.data.dto.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): UserResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): UserResponse

    @GET("auth/profile/{id}")
    suspend fun getUserById(@Path("id") userId: Int): UserResponse

    @PUT("auth/profile/{id}")
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Body request: UpdateUserRequest
    ): ApiResponse // ✅ beda dari GET

    @DELETE("auth/profile/{id}")
    suspend fun deleteUser(@Path("id") userId: Int): ApiResponse
}