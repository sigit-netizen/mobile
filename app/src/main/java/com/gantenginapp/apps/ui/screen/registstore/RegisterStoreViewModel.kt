package com.gantenginapp.apps.ui.screen.registstore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gantenginapp.apps.data.repository.StoreRepository
import com.gantenginapp.apps.data.repository.UserRepository
import com.gantenginapp.apps.data.remote.dto.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegisterStoreViewModel(
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository     // 🔥 Tambahkan ini
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _success = MutableStateFlow<String?>(null)
    val success: StateFlow<String?> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    init {
        loadUserId()
    }

    private fun loadUserId() {
        viewModelScope.launch {
            userRepository.getUserId().collect { id ->
                _userId.value = id
            }
        }
    }


    fun registerStore(
        name: String,
        alamat: String,
        onSuccess: () -> Unit
    ) {
        val uid = _userId.value

        if (uid == null) {
            _error.value = "User ID tidak ditemukan di DataStore."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _success.value = null

            try {
                val request = RegistStoreReq(
                    idUser = uid.toString(),
                    namaToko = name,
                    Alamat = alamat
                )

                val response = storeRepository.registerStore(request)

                if (response.status) {
                    _success.value = response.message
                    onSuccess()
                } else {
                    _error.value = response.message
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "Terjadi kesalahan."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
