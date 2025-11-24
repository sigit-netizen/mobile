// File: app/src/main/java/com/gantenginapp/apps/ui/screen/login/LoginViewModel.kt
package com.gantenginapp.apps.ui.screen.login

import android.content.Context
import android.util.Log // Tambahkan import ini
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gantenginapp.apps.data.dto.LoginRequest
import com.gantenginapp.apps.data.dto.LoginResponse
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException // Import HttpException

class LoginViewModel(
    private val authRepository: AuthRepositoryImpl,
    private val context: Context
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null) // Error global (pesan umum dari API/jaringan, atau error validasi lokal)
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
        // Hapus error validasi lokal saat user mulai mengetik
        if (_usernameLocalError.value != null) _usernameLocalError.value = null
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        // Hapus error validasi lokal saat user mulai mengetik
        if (_passwordLocalError.value != null) _passwordLocalError.value = null
    }

    fun clearStatus() {
        _errorMessage.value = null
        _successMessage.value = null
        // Hapus juga error lokal
        _usernameLocalError.value = null
        _passwordLocalError.value = null
    }

    fun performLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            clearStatus() // Hapus status sebelum login

            // --- Validasi input lokal di awal ---
            var hasLocalError = false // Flag untuk menandai error validasi lokal

            if (_username.value.isBlank()) {
                _usernameLocalError.value = "Username harus diisi."
                hasLocalError = true
            }

            if (_password.value.isBlank()) {
                _passwordLocalError.value = "Password harus diisi."
                hasLocalError = true
            }

            // Jika ada error validasi lokal (kosong), hentikan proses sebelum kirim ke API
            if (hasLocalError) {
//                _errorMessage.value = "Harap isi semua kolom." // ✅ Pesan global untuk error validasi lokal
                _isLoading.value = false
                return@launch
            }

            // Jika validasi lokal lolos (keduanya diisi), kirim request ke API
            try {
                val request = LoginRequest(
                    username = _username.value,
                    password = _password.value
                )

                Log.d("LoginViewModel", "Mengirim request login: $request") // Log request

                val response: LoginResponse = authRepository.login(request)

                Log.d("LoginViewModel", "Respons API diterima: $response") // Log seluruh response
                Log.d("LoginViewModel", "Status: ${response.status}")       // Log status
                Log.d("LoginViewModel", "User: ${response.user}")           // Log user object
                Log.d("LoginViewModel", "Message: ${response.message}")     // Log message

                if (response.status && response.user != null) {
                    Log.d("LoginViewModel", "Login sukses, menyimpan user ID: ${response.user.id}")
                    // ✅ Login sukses
                    _successMessage.value = "Login sukses!"

                    // ✅ Simpan userId ke SharedPreferences
                    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putInt("user_id", response.user.id)
                        apply()
                    }

                    // ✅ Panggil callback untuk navigasi
                    onSuccess()
                } else {
                    Log.d("LoginViewModel", "Login gagal. Status: ${response.status}, User: ${response.user}")
                    // Tangani error login dari API *setelah* validasi lokal lolos
                    val errorMessageFromApi = response.message ?: "Login gagal."
                    // Asumsikan API kamu mengembalikan pesan seperti:
                    // - "User tidak ditemukan"
                    // - "Password salah"
                    when (errorMessageFromApi) {
                        "User tidak ditemukan" -> {
                            // Tandai kolom username sebagai error
                            _usernameLocalError.value = "Username tidak ditemukan." // Pesan spesifik untuk kolom username
//                            _errorMessage.value = "Username tidak ditemukan." // Pesan global umum
                        }
                        "Password salah" -> {
                            // Tandai kolom password sebagai error
                            _passwordLocalError.value = "Password salah." // Pesan spesifik untuk kolom password
//                            _errorMessage.value = "Password salah." // Pesan global umum
                        }
                        else -> {
                            // Error lainnya (misalnya server error)
                            // Jika kamu ingin menandai kolom untuk error ini, kamu bisa menambahkan logika tambahan
                            // Untuk sekarang, hanya tampilkan di error global
                            _errorMessage.value = errorMessageFromApi
                        }
                    }
                }
            } catch (e: HttpException) {
                Log.e("LoginViewModel", "HTTP Error saat login: ${e.code()}, Message: ${e.message()}")
                // Tangani error jaringan sebelum menerima respons JSON (misalnya 404 Not Found, 401 Unauthorized, 500 Internal Server Error)
                when (e.code()) {
                    404 -> {
                        // Asumsikan 404 berarti username tidak ditemukan
                        _usernameLocalError.value = "Username tidak ditemukan."
//                        _errorMessage.value = "Username tidak ditemukan." // Pesan global
                    }
                    401 -> {
                        // Asumsikan 401 berarti password salah
                        _passwordLocalError.value = "Password salah."
//                        _errorMessage.value = "Password salah." // Pesan global
                    }
                    400 -> _errorMessage.value = "username atau password salah" // Bisa karena field kosong, tapi validasi lokal harusnya menangkap ini
                    500 -> _errorMessage.value = "Server mengalami kesalahan internal. Silakan coba lagi nanti."
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception saat login", e) // Log exception
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}