// app/src/main/java/com/gantenginapp/apps/ui/screen/profil/ProfileViewModel.kt
package com.gantenginapp.apps.ui.screen.profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gantenginapp.apps.data.dto.UserResponse
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserProfile(
    val id: Int = 0,
    val username: String = "",
    val noHp: String = "", // ✅ Ganti: phone -> noHp
    val email: String = "",
    val role: String = "",
    val password: String = ""
)

class ProfileViewModel(
    private val authRepository: AuthRepositoryImpl
) : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

    private val _navigateToLogin = MutableStateFlow(false)
    val navigateToLogin: StateFlow<Boolean> = _navigateToLogin

    // Validasi lokal
    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError: StateFlow<String?> = _usernameError

    private val _noHpError = MutableStateFlow<String?>(null) // ✅ Ganti: _phoneError -> _noHpError
    val noHpError: StateFlow<String?> = _noHpError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    // State untuk konfirmasi hapus akun
    private val _showDeleteConfirmation = MutableStateFlow(false)
    val showDeleteConfirmation: StateFlow<Boolean> = _showDeleteConfirmation

    // State untuk snackbar sukses
    private val _showSaveSuccess = MutableStateFlow(false)
    val showSaveSuccess: StateFlow<Boolean> = _showSaveSuccess

    // ✅ Tambahkan state visibility password
    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible

    fun fetchUserProfile(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response: UserResponse = authRepository.getUserById(userId)
                if (response.status && response.user != null) {
                    val user = response.user
                    _userProfile.value = UserProfile(
                        id = user.id,
                        username = user.username,
                        noHp = user.noHP, // ✅ Ganti: phone = user.noHp
                        email = user.email,
                        role = user.role
                    )
                } else {
                    _error.value = response.message ?: "Gagal mengambil data pengguna."
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun startEditing() {
        _isEditing.value = true
        _usernameError.value = null
        _noHpError.value = null // ✅ Ganti: _phoneError -> _noHpError
        _emailError.value = null
        _passwordError.value = null
    }

    fun saveProfile() {
        // Validasi lokal dulu
        var hasError = false

        if (userProfile.value.username.isBlank()) {
            _usernameError.value = "Username tidak boleh kosong"
            hasError = true
        }

        if (userProfile.value.noHp.isBlank()) { // ✅ Ganti: phone -> noHp
            _noHpError.value = "No HP tidak boleh kosong" // ✅ Ganti: _phoneError -> _noHpError
            hasError = true
        } else if (!userProfile.value.noHp.matches(Regex("^[0-9]{10,13}$"))) { // ✅ Ganti: phone -> noHp
            _noHpError.value = "Nomor HP harus 10–13 digit dan hanya berisi angka." // ✅ Ganti: _phoneError -> _noHpError
            hasError = true
        }

        if (userProfile.value.email.isBlank()) {
            _emailError.value = "Email tidak boleh kosong"
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userProfile.value.email).matches()) {
            _emailError.value = "Format email tidak valid"
            hasError = true
        }

        if (userProfile.value.password.isNotBlank()) {
            if (!userProfile.value.password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$"))) {
                _passwordError.value = "Password minimal 4 karakter dan harus mengandung huruf serta angka."
                hasError = true
            }
        }

        if (hasError) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val user = _userProfile.value
                val passwordToSend = if (user.password.isNotBlank()) user.password else null

                val response = authRepository.updateUser(
                    userId = user.id,
                    username = user.username,
                    noHp = user.noHp, // ✅ API tetap terima `phone` (karena backend mengharapkan field `phone`)
                    email = user.email,
                    password = passwordToSend
                )

                if (response.status) {
                    _isEditing.value = false
                    _showSaveSuccess.value = true
                } else {
                    _error.value = response.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAccount(userId: Int) {
        _showDeleteConfirmation.value = true
    }

    fun confirmDeleteAccount(userId: Int) {
        _showDeleteConfirmation.value = false
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = authRepository.deleteUser(userId)
                if (response.status) {
                    _navigateToLogin.value = true
                } else {
                    _error.value = response.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cancelDeleteAccount() {
        _showDeleteConfirmation.value = false
    }

    fun updateUsername(username: String) {
        _userProfile.value = _userProfile.value.copy(username = username)
        if (username.isNotBlank()) {
            _usernameError.value = null
        }
    }

    fun updateNoHp(noHp: String) { // ✅ Ganti: updatePhone -> updateNoHp
        _userProfile.value = _userProfile.value.copy(noHp = noHp) // ✅ Ganti: phone = phone -> noHp = noHp
        if (noHp.isNotBlank() && noHp.matches(Regex("^[0-9]{10,13}$"))) { // ✅ Ganti: phone -> noHp
            _noHpError.value = null // ✅ Ganti: _phoneError -> _noHpError
        }
    }

    fun updateEmail(email: String) {
        _userProfile.value = _userProfile.value.copy(email = email)
        if (email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = null
        }
    }

    fun updatePassword(password: String) {
        _userProfile.value = _userProfile.value.copy(password = password)
        if (password.isBlank() || password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$"))) {
            _passwordError.value = null
        }
    }

    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun cancelEditing() {
        _isEditing.value = false
        _usernameError.value = null
        _noHpError.value = null // ✅ Ganti: _phoneError -> _noHpError
        _emailError.value = null
        _passwordError.value = null
    }

    fun resetNavigateToLogin() {
        _navigateToLogin.value = false
    }

    fun resetSaveSuccess() {
        _showSaveSuccess.value = false
    }
}