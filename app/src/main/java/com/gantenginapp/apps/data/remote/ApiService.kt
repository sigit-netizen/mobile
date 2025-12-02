// app/src/main/java/com/gantenginapp/apps/data/remote/ApiService.kt
package com.gantenginapp.apps.data.remote

import com.gantenginapp.apps.data.remote.dto.*
import retrofit2.http.*
import com.gantenginapp.apps.domain.model.Store
import retrofit2.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): UserResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): UserResponse

    @GET("auth/profile/{id}")
    suspend fun getUserById(@Path("id") userId: String): UserResponse

    @PUT("auth/profile/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body request: UpdateUserRequest
    ): ApiResponse // ✅ beda dari GET

    @DELETE("auth/profile/{id}")
    suspend fun deleteUser(@Path("id") userId: String): ApiResponse

    @POST("auth/regist-store")
    suspend fun registerStore(@Body request: RegistStoreReq): ApiResponse

    @GET("stores")
    suspend fun getAllStore() : StoresResponse

    @GET("stores/{id}")
    suspend fun getStoreAndOtherById(@Path("id") storeId: Int) : StoreAndOthersResponse

    @POST("stores/ngantri")
    suspend fun  ngantri(@Body request: BookingReq) : Response<ApiResponse>

    @GET("admin-store/{id}")
    suspend fun getAdminStore(@Path("id") userId: Int) : Response<StoreAndOthersResponse>

    @PUT("stores/{id}")
    suspend fun updateStore(
        @Path("id") storeId: Int,
        @Body request: StoreUpdateRequest
    ): Response<ApiResponse>

    @POST("stores/{id}/generate")
    suspend fun generate(
        @Path("id") storeId: Int,
        @Body request: GenerateSlotRequest
    ): Response<ApiResponse>

}