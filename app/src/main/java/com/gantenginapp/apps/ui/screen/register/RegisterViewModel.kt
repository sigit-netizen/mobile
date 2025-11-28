package com.gantenginapp.apps.ui.screen.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gantenginapp.apps.data.remote.dto.*
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(
    private val authRepository: AuthRepositoryImpl
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _noHp = MutableStateFlow("")
    val noHp: StateFlow<String> = _noHp

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    // ✅ Tambahkan state visibility password
    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // ✅ Tambahkan state untuk dialog sukses/gagal
    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    // --- State untuk error validasi lokal (diubah nama agar sesuai RegisterScreen.kt) ---
    private val _usernameError = MutableStateFlow<String?>(null)  // ✅ Ganti: _usernameLocalError -> _usernameError
    val usernameError: StateFlow<String?> = _usernameError       // ✅ Ganti: usernameLocalError -> usernameError

    private val _noHpError = MutableStateFlow<String?>(null)     // ✅ Ganti: _noHpLocalError -> _noHpError
    val noHpError: StateFlow<String?> = _noHpError              // ✅ Ganti: noHpLocalError -> noHpError

    private val _emailError = MutableStateFlow<String?>(null)    // ✅ Ganti: _emailLocalError -> _emailError
    val emailError: StateFlow<String?> = _emailError            // ✅ Ganti: emailLocalError -> emailError

    private val _passwordError = MutableStateFlow<String?>(null) // ✅ Ganti: _passwordLocalError -> _passwordError
    val passwordError: StateFlow<String?> = _passwordError      // ✅ Ganti: passwordLocalError -> passwordError

    fun onUsernameChange(username: String) {
        _username.value = username
        if (_usernameError.value != null) _usernameError.value = null  // ✅ Ganti: _usernameLocalError -> _usernameError
    }

    fun onNoHpChange(noHp: String) {
        _noHp.value = noHp
        if (_noHpError.value != null) _noHpError.value = null         // ✅ Ganti: _noHpLocalError -> _noHpError
    }

    fun onEmailChange(email: String) {
        _email.value = email
        if (_emailError.value != null) _emailError.value = null       // ✅ Ganti: _emailLocalError -> _emailError
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        if (_passwordError.value != null) _passwordError.value = null // ✅ Ganti: _passwordLocalError -> _passwordError
    }

    // ✅ Fungsi toggle visibility password
    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun clearStatus() {
        _errorMessage.value = null
        _usernameError.value = null                                   // ✅ Ganti: _usernameLocalError -> _usernameError
        _noHpError.value = null                                       // ✅ Ganti: _noHpLocalError -> _noHpError
        _emailError.value = null                                      // ✅ Ganti: _emailLocalError -> _emailError
        _passwordError.value = null                                   // ✅ Ganti: _passwordLocalError -> _passwordError
        _showSuccessDialog.value = false
        _showErrorDialog.value = false
    }

    fun performRegister() {
        viewModelScope.launch {
            _isLoading.value = true
            clearStatus()

            var hasLocalError = false

            if (_username.value.isBlank()) {
                _usernameError.value = "Username harus diisi."        // ✅ Ganti: _usernameLocalError -> _usernameError
                hasLocalError = true
            }

            if (_noHp.value.isBlank()) {
                _noHpError.value = "No HP harus diisi."               // ✅ Ganti: _noHpLocalError -> _noHpError
                hasLocalError = true
            } else if (!_noHp.value.matches(Regex("^[0-9]{10,13}$"))) {
                _noHpError.value = "No HP harus 10-13 digit angka."  // ✅ Ganti: _noHpLocalError -> _noHpError
                hasLocalError = true
            }

            if (_email.value.isBlank()) {
                _emailError.value = "Email harus diisi."              // ✅ Ganti: _emailLocalError -> _emailError
                hasLocalError = true
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
                _emailError.value = "Format email tidak valid."       // ✅ Ganti: _emailLocalError -> _emailError
                hasLocalError = true
            }

            if (_password.value.isBlank()) {
                _passwordError.value = "Password harus diisi."        // ✅ Ganti: _passwordLocalError -> _passwordError
                hasLocalError = true
            } else if (!_password.value.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$"))) {
                _passwordError.value = "Password minimal 4 karakter dan harus mengandung huruf serta angka." // ✅ Ganti: _passwordLocalError -> _passwordError
                hasLocalError = true
            }

            if (hasLocalError) {
                _isLoading.value = false
                return@launch
            }

            try {
                val request = RegisterRequest(
                    username = _username.value,
                    noHp = _noHp.value,
                    email = _email.value,
                    password = _password.value
                )

                Log.d("RegisterViewModel", "Mengirim request register: $request")

                val response: UserResponse = authRepository.register(request)

                Log.d("RegisterViewModel", "Respons API diterima: $response")
                Log.d("RegisterViewModel", "Status: ${response.status}")
                Log.d("RegisterViewModel", "User: ${response.user}")
                Log.d("RegisterViewModel", "Message: ${response.message}")

                if (response.status && response.user != null) {
                    Log.d("RegisterViewModel", "Register sukses, user ID: ${response.user.id}")
                    _showSuccessDialog.value = true
                } else {
                    Log.d("RegisterViewModel", "Register gagal. Status: ${response.status}, User: ${response.user}")
                    val errorMessageFromApi = response.message ?: "Register gagal."
                    // ✅ Hanya tangani pesan "Email sudah digunakan"
                    if (errorMessageFromApi == "Email sudah digunakan.") {
                        _emailError.value = errorMessageFromApi        // ✅ Ganti: _emailLocalError -> _emailError
                        _showErrorDialog.value = true
                    } else {
                        // ✅ Error lainnya tampilkan di error global
                        _errorMessage.value = errorMessageFromApi
                        _showErrorDialog.value = true
                    }
                }
            } catch (e: HttpException) {
                Log.e("RegisterViewModel", "HTTP Error saat register: ${e.code()}, Message: ${e.message()}")
                when (e.code()) {
                    409 -> { // Conflict: Hanya untuk email yang sama
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorMessageFromApi = try {
                            val json = org.json.JSONObject(errorBody!!)
                            json.getString("message")
                        } catch (parseError: Exception) {
                            "Data sudah digunakan."
                        }
                        // ✅ Hanya tangani pesan "Email sudah digunakan"
                        if (errorMessageFromApi == "Email sudah digunakan.") {
                            _emailError.value = errorMessageFromApi   // ✅ Ganti: _emailLocalError -> _emailError
                        } else {
                            _errorMessage.value = errorMessageFromApi
                        }
                        _showErrorDialog.value = true
                    }
                    400 -> _errorMessage.value = "Data tidak valid."
                    500 -> _errorMessage.value = "Server mengalami kesalahan internal. Silakan coba lagi nanti."
                }
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Exception saat register", e)
                _errorMessage.value = e.message
                _showErrorDialog.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ Fungsi untuk menutup dialog
    fun dismissSuccessDialog() {
        _showSuccessDialog.value = false
    }

    fun dismissErrorDialog() {
        _showErrorDialog.value = false
    }
}