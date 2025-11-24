// File: app/src/main/java/com/gantenginapp/apps/ui/screen/register/RegisterViewModel.kt
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
import java.util.regex.Pattern

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

    private val _errorMessage = MutableStateFlow<String?>(null) // Error umum (backend, jaringan)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val _isSuccess = MutableStateFlow<Boolean?>(null)
    val isSuccess: StateFlow<Boolean?> = _isSuccess

    // --- State baru untuk error validasi lokal ---
    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError: StateFlow<String?> = _usernameError

    private val _noHpError = MutableStateFlow<String?>(null)
    val noHpError: StateFlow<String?> = _noHpError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    fun onUsernameChange(value: String) {
        _username.value = value
        // Hapus error saat user mulai mengetik
        if (_usernameError.value != null) _usernameError.value = null
    }

    fun onPasswordChange(value: String) {
        _password.value = value
        if (_passwordError.value != null) _passwordError.value = null
    }

    fun onEmailChange(value: String) {
        _email.value = value
        if (_emailError.value != null) _emailError.value = null
    }

    fun onNoHpChange(value: String) {
        _noHp.value = value
        if (_noHpError.value != null) _noHpError.value = null
    }

    fun clearStatus() {
        _errorMessage.value = null
        _successMessage.value = null
        _isSuccess.value = null
        // Juga hapus error lokal
        _usernameError.value = null
        _noHpError.value = null
        _emailError.value = null
        _passwordError.value = null
    }

    fun performRegister(onSuccess: (User) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            clearStatus() // Hapus semua error sebelum mulai

            // --- Validasi input lokal SESUAI ATURAN API ---
            var hasLocalError = false // Flag untuk menandai jika ada error validasi lokal

            // 1. Validasi Username
            if (_username.value.isBlank()) {
                _usernameError.value = "Username harus diisi."
                hasLocalError = true
            }

            // 2. Validasi No HP: angka saja 10–13 digit
            if (_noHp.value.isNotBlank()) { // Validasi hanya jika tidak kosong (kamu bisa ubah jadi wajib)
                val hpRegex = Pattern.compile("^[0-9]{10,13}$")
                if (!hpRegex.matcher(_noHp.value).matches()) {
                    _noHpError.value = "Nomor HP harus 10–13 digit dan hanya berisi angka."
                    hasLocalError = true
                }
            } else {
                _noHpError.value = "No HP harus diisi." // Jika kamu ingin No HP wajib
                hasLocalError = true
            }

            // 3. Validasi Email: format email
            if (_email.value.isNotBlank()) { // Validasi hanya jika tidak kosong
                val emailRegex = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                if (!emailRegex.matcher(_email.value).matches()) {
                    _emailError.value = "Format email tidak valid."
                    hasLocalError = true
                }
            } else {
                _emailError.value = "Email harus diisi." // Jika kamu ingin Email wajib
                hasLocalError = true
            }

            // 4. Validasi Password: huruf + angka, minimal 4 karakter
            if (_password.value.isNotBlank()) { // Validasi hanya jika tidak kosong
                val passwordRegex = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$")
                if (!passwordRegex.matcher(_password.value).matches()) {
                    _passwordError.value = "Password minimal 4 karakter dan harus mengandung huruf serta angka."
                    hasLocalError = true
                }
            } else {
                _passwordError.value = "Password harus diisi." // Jika kamu ingin Password wajib
                hasLocalError = true
            }

            // Jika ada error validasi lokal, hentikan proses
            if (hasLocalError) {
                _isLoading.value = false
                return@launch
            }

            // Jika semua validasi lokal lolos, buat request
            val request = RegisterRequest(
                username = _username.value,
                password = _password.value,
                email = _email.value,
                noHp = _noHp.value
            )

            try {
                val response = authRepository.register(request)

                // --- Penanganan respons backend (hanya jika request dikirim) ---
                if (response.status) {
                    val user = response.data!! // Asumsi data tidak null jika status true
                    _successMessage.value = response.message ?: "Registrasi berhasil!"
                    _isSuccess.value = true
                    onSuccess(user)
                } else {
                    // Jika request dikirim tapi API kembalikan status false
                    // Gunakan pesan dari API untuk error selain validasi input (misalnya email sudah dipakai)
                    val errorMsg = when (response.message) {
                        "Email sudah digunakan." -> "Email ini sudah terdaftar. Silakan gunakan email lain."
                        else -> "Registrasi gagal: ${response.message}"
                    }
                    // Tampilkan di error global
                    _errorMessage.value = errorMsg
                }

            } catch (e: IOException) {
                _errorMessage.value = "Tidak dapat terhubung ke server. Periksa koneksi internet Anda."
            } catch (e: HttpException) {
                val errorMsg = when (e.code()) {
                    409 -> "Email sudah digunakan."
                    500 -> "Server mengalami kesalahan internal. Silakan coba lagi nanti."
                    else -> "Terjadi kesalahan jaringan (${e.code()})."
                }
                _errorMessage.value = errorMsg
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan tak terduga: ${e.message}"
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