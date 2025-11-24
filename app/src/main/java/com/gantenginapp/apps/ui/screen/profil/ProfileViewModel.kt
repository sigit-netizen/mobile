    package com.gantenginapp.apps.ui.screen.profil

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.gantenginapp.apps.data.dto.User
    import com.gantenginapp.apps.data.dto.UserResponse
    import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.launch

    data class UserProfile(
        val id: Int = 0,
        val username: String = "",
        val phone: String = "",
        val email: String = "",
        val role: String = "",
        val password: String = "" // Jangan tampilkan di UI produksi
    )

    class ProfileViewModel(
        private val authRepository: AuthRepositoryImpl
    ) : ViewModel() {

        private val _userProfile = MutableStateFlow(UserProfile())
        val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

        private val _error = MutableStateFlow<String?>(null)
        val error: StateFlow<String?> = _error.asStateFlow()

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
                            phone = user.noHP,
                            email = user.email,
                            role = user.role,
                        )
                    } else {
                        // Gunakan pesan dari server jika tersedia
                        _error.value = response.message ?: "Gagal mengambil data pengguna."
                    }
                } catch (e: Exception) {
                    _error.value = e.message
                } finally {
                    _isLoading.value = false
                }
            }
        }

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

        fun setUserProfile(user: User) {
            _userProfile.value = UserProfile(
                id = user.id,
                username = user.username,
                phone = user.noHP,
                email = user.email,
                role = user.role,
            )
        }
    }