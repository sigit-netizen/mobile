package com.gantenginapp.apps.data.repository

import com.gantenginapp.apps.data.local.UserPreferences
import com.gantenginapp.apps.data.remote.ApiService
import com.gantenginapp.apps.domain.model.User
import kotlinx.coroutines.flow.Flow
import android.content.Context

class UserRepository(
    private val prefs: UserPreferences,
    private val api: ApiService,
    private val context: Context
) {
    fun getUser(): Flow<User> = prefs.userFlow

    suspend fun saveUser(user: User) = prefs.saveUser(user)

    suspend fun clearUser() = prefs.clearUser()


    fun getUserId(): Flow<String?> = prefs.getUserId()

}