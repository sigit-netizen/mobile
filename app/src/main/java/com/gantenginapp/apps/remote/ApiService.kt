package com.gantenginapp.apps.remote

import android.os.Message
import  retrofit2.http.GET
import com.gantenginapp.apps.model.User
import retrofit2.http.Body
import retrofit2.http.POST



data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val status: String,
    val data: User?
)


interface ApiService {
    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}




