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
import com.gantenginapp.apps.data.repository.DataRefresher
import kotlinx.coroutines.flow.asStateFlow
import com.gantenginapp.apps.data.repository.StoreRepository
data class StoreItem(
    val id: Int,
    val name: String,
    val address: String,
    val price: String,
    val status: String
)

class HomeViewModel (
    private val userRepository: UserRepository,
    private val dataRefresher: DataRefresher,
    private val storeRepository : StoreRepository
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

    // Ini Fitur Refresh
    private val _isRefreshingLoading = MutableStateFlow(false)
    val isRefreshingLoading = _isRefreshingLoading.asStateFlow()

    fun loadDataStoreAndUser () {
        viewModelScope.launch {
            _isRefreshingLoading.value = true
            dataRefresher.refreshUser()
            delay(3000L)
            loadUser()
            _isRefreshingLoading.value = false
        }
    }


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

    fun loadData() {
        viewModelScope.launch {
            _isRefreshingLoading.value = true
            try {
                val response = storeRepository.getAllStores()
                delay(3000)
                val dataMapped = response.data
                    .filter { store ->
                        listOf(
                            store.idStore,
                            store.storeName,
                            store.alamat,
                            store.price,
                            store.status ?: 0,
                            store.totalAntrian ?: 0,
                            store.openingHours,
                            store.closingTime,
                            store.idUser
                        ).all { it != null }  // <-- kalau ada 1 null, otomatis tidak lolos
                    }
                    .map { store ->
                        StoreItem(
                            id = store.idStore!!,
                            name = store.storeName!!,
                            address = store.alamat!!,
                            price = "Rp.${store.price!!}",
                            status = if (store.status == 1) "${store.totalAntrian}" else "tutup"
                        )
                    }
                    .ifEmpty {
                        listOf(
                            StoreItem(
                                id = -1,
                                name = "Belum ada toko",
                                address = "",
                                price = "",
                                status = ""
                            )
                        )
                    }
                _allStores.value = dataMapped

            } catch (e: Exception) {
                e.printStackTrace()
            }


            _isRefreshingLoading.value = false
        }

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