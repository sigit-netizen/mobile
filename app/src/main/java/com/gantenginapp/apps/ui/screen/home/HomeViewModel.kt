package com.gantenginapp.apps.ui.screen.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _username = MutableStateFlow("Ananda") // Ganti dengan data sebenarnya
    val username: StateFlow<String> = _username

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // ✅ Fix: Tambahkan tipe data
    private val _data = MutableStateFlow<List<String>>(emptyList())
    val data: StateFlow<List<String>> = _data

    init {
        loadData()
    }

    private fun loadData() {
        // Contoh: load data dari repository
        _isLoading.value = true
        // Misalnya: _data.value = repository.getData()
        _data.value = listOf("Item 1", "Item 2", "Item 3") // Dummy data
        _isLoading.value = false
    }

    fun onProfileClick() {
        // ✅ Logika saat profile diklik
        // Contoh: navigasi ke ProfileActivity
    }

    fun onDetailClick() {
        // Contoh: logika saat detail diklik
    }

    fun onLogoutClick() {
        // Contoh: clear session di repository
    }
}