package com.gantenginapp.apps.data.repository

import com.gantenginapp.apps.data.local.UserPreferences
import com.gantenginapp.apps.domain.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val prefs: UserPreferences) {

    suspend fun saveUser(user: User) = prefs.saveUser(user)

    fun getUser(): Flow<User> = prefs.user

    suspend fun clearUser() = prefs.clearUser()
}
