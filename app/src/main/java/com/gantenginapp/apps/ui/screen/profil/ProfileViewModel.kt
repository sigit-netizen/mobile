package com.gantenginapp.apps.ui.screen.profil

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

data class UserProfile(
    val username: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "" // Jika kamu perlu menyimpan password di UI (tidak disarankan)
)

class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    val username: StateFlow<String> = _userProfile.map { it.username }
    val phone: StateFlow<String> = _userProfile.map { it.phone }
    val email: StateFlow<String> = _userProfile.map { it.email }
    val password: StateFlow<String> = _userProfile.map { it.password } // Jika perlu

    fun updateUsername(username: String) {
        _userProfile.value = _userProfile.value.copy(username = username)
    }

    fun updatePhone(phone: String) {
        _userProfile.value = _userProfile.value.copy(phone = phone)
    }

    fun updateEmail(email: String) {
        _userProfile.value = _userProfile.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _userProfile.value = _userProfile.value.copy(password = password)
    }

    // Fungsi untuk mengisi semua data sekaligus (misalnya dari API)
    fun setUserProfile(username: String, phone: String, email: String, password: String = "") {
        _userProfile.value = UserProfile(username, phone, email, password)
    }
}