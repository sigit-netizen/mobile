package com.gantenginapp.apps.ui.screen.adminstore

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
import kotlinx.coroutines.flow.asStateFlow
import com.gantenginapp.apps.data.repository.StoreRepository
import com.gantenginapp.apps.domain.model.Store
import com.gantenginapp.apps.domain.model.Antrian
import com.gantenginapp.apps.data.remote.dto.StoreUpdateRequest
class AdminStoreViewModel (
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository
) : ViewModel () {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    private val _antrian = MutableStateFlow<List<Antrian>>(emptyList())
    val antrian = _antrian.asStateFlow()
    private val _store = MutableStateFlow(Store())
    val store = _store.asStateFlow()
    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()
    fun clearMessage() {
        _message.value = null
    }



    private val _isRefreshingLoading = MutableStateFlow(false)
    val isRefreshingLoading = _isRefreshingLoading.asStateFlow()

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

    fun updateStore(data: StoreUpdateRequest) {
        viewModelScope.launch {
            try {
                _isRefreshingLoading.value = true

                val idStore = store.value.idStore ?: return@launch
                val response = storeRepository.updateStore(idStore, data)

                if (response.isSuccessful) {
                    _message.value = "Berhasil update store!"
                    loadDataAdminStore()
                } else {
                    _message.value = "Gagal update: ${response.code()}"
                }

            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            } finally {
                _isRefreshingLoading.value = false
            }
        }
    }


    fun loadDataAdminStore () {
        viewModelScope.launch {
            try {
                _isRefreshingLoading.value = true
                delay(3000)

                val response  = storeRepository.getStoreAdmin(user.value?.id!!.toInt())
                _store.value = response.body()?.store ?: Store()

                val dataAntrianMapped: List<Antrian> = response.body()?.antrian
                    ?.map { antrian ->
                        Antrian(
                            idStore = antrian.idStore ?: 0,
                            customerName = antrian.customerName ?: "Kosong",
                            noHp = antrian.noHp ?: "",
                            waktu = antrian.waktu ?: "",
                            status = antrian.status ?: 0
                        )
                    } ?: emptyList()

                if(dataAntrianMapped.isEmpty()) {
                    _message.value = "Belum ada antrian"
                }

                _antrian.value = dataAntrianMapped
                _isRefreshingLoading.value = true



            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                _isRefreshingLoading.value = false
            }
            _isRefreshingLoading.value = false
        }
    }



}