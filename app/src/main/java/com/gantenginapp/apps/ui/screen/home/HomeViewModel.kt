package com.gantenginapp.apps.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharingStarted
import com.gantenginapp.apps.data.repository.UserRepository
import com.gantenginapp.apps.data.remote.dto.User
import androidx.lifecycle.viewModelScope
import com.gantenginapp.apps.data.repository.DataRefresher
data class StoreItem(
    val id: Int,
    val name: String,
    val address: String,
    val price: String,
    val status: String
)

class HomeViewModel (
    private val userRepository: UserRepository
) : ViewModel() {

    // Ini ngambil data user yang akan kita gunakan untuk seleksi nanti
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    init {
        loadUser()
    }
    private fun loadUser() {
        viewModelScope.launch {
            userRepository.getUser().collect {
                _user.value = it
            }
        }
    }
    //Ini Akhir load user


    private val _username = MutableStateFlow("Ananda")
    val username: StateFlow<String> = _username

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _allStores = MutableStateFlow<List<StoreItem>>(emptyList())
    val filteredStores: StateFlow<List<StoreItem>> = combine(_allStores, _searchQuery) { stores, query ->
        if (query.isBlank()) stores else stores.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog

    init {
        loadData()
    }

    private fun loadData() {
        _isLoading.value = true
        _allStores.value = listOf(
            StoreItem(1, "Barber Ananda", "Jl. Merdeka No.1", "Rp.10000", "tersedia"),
            StoreItem(2, "Tukang Potong Rapi", "Jl. Sudirman", "Rp.15000", "penuh"),
            StoreItem(3, "Gantengin Barber", "Jl. Gatot Subroto", "Rp.12000", "tutup"),
            StoreItem(4, "Potong Cepat", "Jl. Diponegoro", "Rp.8000", "tersedia"),
            StoreItem(5, "Salon Keren", "Jl. Ahmad Yani", "Rp.20000", "tersedia"),
            StoreItem(6, "Fade Master", "Jl. Veteran", "Rp.18000", "penuh"),
            StoreItem(7, "Trim & Go", "Jl. Pahlawan", "Rp.9000", "tutup"),
            StoreItem(8, "Clean Cut", "Jl. Flores", "Rp.11000", "tersedia"),
            StoreItem(9, "Sharp Line", "Jl. Bali", "Rp.13000", "tersedia"),
            StoreItem(10, "Edge Barber", "Jl. Sumatra", "Rp.16000", "penuh")
        )
        _isLoading.value = false
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun showLogoutDialog() {
        _showLogoutDialog.value = true
    }

    fun dismissLogoutDialog() {
        _showLogoutDialog.value = false
    }
}