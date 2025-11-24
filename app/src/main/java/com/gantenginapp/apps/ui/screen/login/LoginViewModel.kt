package com.gantenginapp.apps.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gantenginapp.apps.data.dto.LoginRequest
import com.gantenginapp.apps.data.remote.RetrofitClient
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import com.gantenginapp.apps.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepositoryImpl(RetrofitClient.instance)

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

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

    fun clearStatus() {
        _errorMessage.value = null
        _successMessage.value = null
        _isSuccess.value = null
    }

    fun performLogin(onSuccess: (User) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            clearStatus()

            try {
                if (_username.value.isBlank()) {
                    setError("Username harus diisi.")
                    return@launch
                }
                if (_password.value.isBlank()) {
                    setError("Password harus diisi.")
                    return@launch
                }

                val response = authRepository.login(LoginRequest(_username.value, _password.value))

                if (response.status) {
                    val user = response.data
                    if (user != null) {
                        _successMessage.value = "Login berhasil! Selamat datang ${user.username}"
                        _isSuccess.value = true
                        onSuccess(user)
                    } else {
                        setError("Login gagal: data user tidak tersedia.")
                    }
                } else {
                    val msg = when (response.message) {
                        "User tidak ditemukan" -> "Username tidak ditemukan."
                        "Password salah" -> "Password salah."
                        else -> "Login gagal: ${response.message}"
                    }
                    setError(msg)
                }

            } catch (e: IOException) {
                setError("Tidak dapat terhubung ke server. Periksa koneksi internet Anda.")
            } catch (e: HttpException) {
                val msg = when (e.code()) {
                    401 -> "Username atau password salah."
                    404 -> "User tidak ditemukan."
                    500 -> "Server mengalami kesalahan. Coba lagi nanti."
                    else -> "Terjadi kesalahan jaringan (${e.code()})."
                }
                setError(msg)
            } catch (e: Exception) {
                setError("Login gagal: ${e.message}")
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