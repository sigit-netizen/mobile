package com.gantenginapp.apps.ui.screen.StoreBarber

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gantenginapp.apps.data.remote.dto.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.gantenginapp.apps.data.repository.StoreRepository
import com.gantenginapp.apps.domain.model.Store
import kotlinx.coroutines.flow.asStateFlow
import com.gantenginapp.apps.domain.model.Antrian
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.gantenginapp.apps.data.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow


class BarberStoreViewModel(
    private val storeRepository: StoreRepository,
    private val storeId: Int,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _isRefreashingLoading = MutableStateFlow(false)
    val isRefreshingLoading = _isRefreashingLoading.asStateFlow()
    private val _store = MutableStateFlow(Store())
    val store = _store.asStateFlow()

    // Data User Now
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




    private val _antrian = MutableStateFlow<List<Antrian>>(emptyList())
    val antrian = _antrian.asStateFlow()


    fun loadDataStore () {
        viewModelScope.launch {
            try {
                _isRefreashingLoading.value = true
                delay(3000)
                val response = storeRepository.getStoreAndOthers(storeId)
                _store.value = response.store
                val dataAntrianMapped = response.antrian.map { antrian ->
                    Antrian(
                        idAntrian = antrian.idAntrian,
                        idStore = antrian.idStore,
                        customerName = antrian.customerName,
                        noHp = antrian.noHp,
                        waktu = antrian.waktu,
                        status = antrian.status
                    )
                }

                _antrian.value = dataAntrianMapped
            } catch (e: Exception)  {
                e.printStackTrace()
            } finally {
                _isRefreashingLoading.value = false
            }
        }
    }

    fun postAntrian(
        customerName: String,
        noHp: String,
    ) {
        viewModelScope.launch {
            try {
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(Date())

                _isRefreashingLoading.value = true
                delay(1000)
                val request = BookingReq(
                    idStore = storeId,
                    idUser = user.value?.id.orEmpty(),
                    customerName = customerName,
                    noHp = noHp,
                    waktu = currentTime
                )
                val response = storeRepository.ngantri(request)
                if (response.status) {
                    loadDataStore()
                } else {
                    println("Gagal Ngantri")
                }

            } catch (e:Exception) {
                e.printStackTrace()

            } finally {
                _isRefreashingLoading.value = false
            }
        }

    }
}