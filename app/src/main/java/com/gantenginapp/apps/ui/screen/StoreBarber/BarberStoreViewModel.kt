package com.gantenginapp.apps.ui.screen.StoreBarber

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.gantenginapp.apps.data.repository.StoreRepository
import com.gantenginapp.apps.domain.model.Store
import kotlinx.coroutines.flow.asStateFlow
import com.gantenginapp.apps.domain.model.Antrian
class BarberStoreViewModel(
    private val storeRepository: StoreRepository,
    private val storeId: Int
) : ViewModel() {
    private val _isRefreashingLoading = MutableStateFlow(false)
    val isRefreshingLoading = _isRefreashingLoading.asStateFlow()
    private val _store = MutableStateFlow(Store())
    val store = _store.asStateFlow()


    private val _antrian = MutableStateFlow<List<Antrian>>(emptyList())
    val antrian = _antrian.asStateFlow()


    fun loadDataStore () {
        viewModelScope.launch {
            _isRefreashingLoading.value = true
            try {
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
}