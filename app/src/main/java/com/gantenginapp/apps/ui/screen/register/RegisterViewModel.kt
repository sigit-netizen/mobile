package com.gantenginapp.apps.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gantenginapp.apps.data.dto.RegisterRequest
import com.gantenginapp.apps.data.remote.RetrofitClient
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import com.gantenginapp.apps.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel : ViewModel() {

    private val authRepository = AuthRepositoryImpl(RetrofitClient.instance)

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _noHp = MutableStateFlow("")
    val noHp: StateFlow<String> = _noHp

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val _isSuccess = MutableStateFlow<Boolean?>(null)
    val isSuccess: StateFlow<Boolean?> = _isSuccess

    fun onUsernameChange(value: String) { _username.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onNoHpChange(value: String) { _noHp.value = value }

    fun clearStatus() {
        _errorMessage.value = null
        _successMessage.value = null
        _isSuccess.value = null
    }

    fun performRegister(onSuccess: (User) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            clearStatus()

            try {
                // Validasi input lokal
                if (_username.value.isBlank()) {
                    setError("Username harus diisi.")
                    return@launch
                }
                if (_password.value.isBlank()) {
                    setError("Password harus diisi.")
                    return@launch
                }
                if (_email.value.isBlank()) {
                    setError("Email harus diisi.")
                    return@launch
                }
                if (_noHp.value.isBlank()) {
                    setError("No HP harus diisi.")
                    return@launch
                }

                val request = RegisterRequest(
                    username = _username.value,
                    password = _password.value,
                    email = _email.value,
                    noHp = _noHp.value
                )

                // ✅ GANTI: dari RetrofitClient langsung → ke repository
                val response = authRepository.register(request)

                // --- Penanganan respons backend ---
                if (response.status) {
                    val user = response.data!!
                    _successMessage.value = response.message ?: "Registrasi berhasil!"
                    _isSuccess.value = true
                    onSuccess(user)
                } else {
                    // Jika status false, cek pesan backend
                    val errorMsg = when (response.message) {
                        "Email sudah digunakan." -> "Email ini sudah terdaftar. Silakan gunakan email lain."
                        else -> "Registrasi gagal: ${response.message}"
                    }
                    setError(errorMsg)
                }

            } catch (e: IOException) {
                setError("Tidak dapat terhubung ke server. Periksa koneksi internet Anda.")
            } catch (e: HttpException) {
                val errorMsg = when (e.code()) {
                    400 -> "Permintaan tidak valid. Email sudah digunakan. Periksa kembali data anda."
                    409 -> "Email atau username sudah digunakan."
                    500 -> "Server mengalami kesalahan internal. Silakan coba lagi nanti."
                    else -> "Terjadi kesalahan jaringan (${e.code()})."
                }
                setError(errorMsg)
            } catch (e: Exception) {
                setError("Terjadi kesalahan tak terduga: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setError(message: String) {
        _errorMessage.value = message
        _isSuccess.value = false
    }
}