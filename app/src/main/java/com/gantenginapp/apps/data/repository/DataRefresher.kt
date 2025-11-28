package com.gantenginapp.apps.data.repository

import com.gantenginapp.apps.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.gantenginapp.apps.domain.Repository.AuthRepository
import com.gantenginapp.apps.data.remote.ApiService
import kotlinx.coroutines.flow.first

class DataRefresher (
    private val userRepository: UserRepository,
    private val apiService: ApiService
) {
    fun refreshUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = userRepository.getUserId().first()
                if (userId != null) {
                    val response = apiService.getUserById(userId)
                    val user = response.user
                    if (user != null) {
                        userRepository.saveUser(user)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

