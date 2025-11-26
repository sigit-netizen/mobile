// File: app/src/main/java/com/gantenginapp/apps/ui/screen/login/LoginViewModel.kt
package com.gantenginapp.apps.ui.screen.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gantenginapp.apps.data.dto.LoginRequest
import com.gantenginapp.apps.data.dto.LoginResponse
import com.gantenginapp.apps.data.dto.UserResponse
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val authRepository: AuthRepositoryImpl,
    private val context: Context
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // ✅ Tambahkan state visibility password
    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    // --- State untuk error validasi lokal ---
    private val _usernameLocalError = MutableStateFlow<String?>(null)
    val usernameLocalError: StateFlow<String?> = _usernameLocalError

    private val _passwordLocalError = MutableStateFlow<String?>(null)
    val passwordLocalError: StateFlow<String?> = _passwordLocalError

    fun onUsernameChange(username: String) {
        _username.value = username
        if (_usernameLocalError.value != null) _usernameLocalError.value = null
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        if (_passwordLocalError.value != null) _passwordLocalError.value = null
    }

    // ✅ Fungsi toggle visibility password
    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun clearStatus() {
        _errorMessage.value = null
        _successMessage.value = null
        _usernameLocalError.value = null
        _passwordLocalError.value = null
    }

    fun performLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            clearStatus()

            var hasLocalError = false

            if (_username.value.isBlank()) {
                _usernameLocalError.value = "Username harus diisi."
                hasLocalError = true
            }

            if (_password.value.isBlank()) {
                _passwordLocalError.value = "Password harus diisi."
                hasLocalError = true
            }

            if (hasLocalError) {
                _isLoading.value = false
                return@launch
            }

            try {
                val request = LoginRequest(
                    username = _username.value,
                    password = _password.value
                )

                Log.d("LoginViewModel", "Mengirim request login: $request")

                val response: UserResponse = authRepository.login(request)

                Log.d("LoginViewModel", "Respons API diterima: $response")
                Log.d("LoginViewModel", "Status: ${response.status}")
                Log.d("LoginViewModel", "User: ${response.user}")
                Log.d("LoginViewModel", "Message: ${response.message}")

                if (response.status && response.user != null) {
                    Log.d("LoginViewModel", "Login sukses, menyimpan user ID: ${response.user.id}")
                    _successMessage.value = "Login sukses!"

                    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putInt("user_id", response.user.id)
                        apply()
                    }

                    onSuccess()
                } else {
                    Log.d("LoginViewModel", "Login gagal. Status: ${response.status}, User: ${response.user}")
                    val errorMessageFromApi = response.message ?: "Login gagal."
                    when (errorMessageFromApi) {
                        "User tidak ditemukan" -> {
                            _usernameLocalError.value = "Username tidak ditemukan."
                        }
                        "Password salah" -> {
                            _passwordLocalError.value = "Password salah."
                        }
                        else -> {
                            _errorMessage.value = errorMessageFromApi
                        }
                    }
                }
            } catch (e: HttpException) {
                Log.e("LoginViewModel", "HTTP Error saat login: ${e.code()}, Message: ${e.message()}")
                when (e.code()) {
                    404 -> {
                        _usernameLocalError.value = "Username tidak ditemukan."
                    }
                    401 -> {
                        _passwordLocalError.value = "Password salah."
                    }
                    400 -> _errorMessage.value = "username atau password salah"
                    500 -> _errorMessage.value = "Server mengalami kesalahan internal. Silakan coba lagi nanti."
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception saat login", e)
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}