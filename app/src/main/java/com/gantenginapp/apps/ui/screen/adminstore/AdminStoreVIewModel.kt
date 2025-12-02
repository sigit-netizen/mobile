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
    import com.gantenginapp.apps.data.remote.dto.*
    import org.threeten.bp.LocalTime
    import org.threeten.bp.format.DateTimeFormatter
    import android.util.Log

    class AdminStoreViewModel (
        private val storeRepository: StoreRepository,
        private val userRepository: UserRepository
    ) : ViewModel() {
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

        // Generate antrian
        fun generateAntrian(storeId: Int) {
            viewModelScope.launch {
                try {
                    _isRefreshingLoading.value = true

                    val dataStore = _store.value

                    val request = GenerateSlotRequest(
                        opening_hours = dataStore.openingHours ?: "",
                        closing_time = dataStore.closingTime ?: "",
                        durasi = dataStore.durasi ?: 0
                    )

                    val response = storeRepository.generate(storeId, request)

                    if (response.isSuccessful) {
                        _message.value = response.body()?.message ?: "Berhasil generate"
                        loadDataAdminStore()
                    } else {
                        _message.value = "Gagal generate: ${response.code()}"
                    }

                } catch (e: Exception) {
                    _message.value = "Error: ${e.message}"
                } finally {
                    _isRefreshingLoading.value = false
                }
            }
        }



        fun loadDataAdminStore() {
            viewModelScope.launch {
                try {
                    _isRefreshingLoading.value = true
                    delay(1000)

                    val response = storeRepository.getStoreAdmin(user.value?.id!!.toInt())
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

                    fun getCurrentTimeString(): String {
                        val cal = java.util.Calendar.getInstance()
                        val hour = cal.get(java.util.Calendar.HOUR_OF_DAY)
                        val minute = cal.get(java.util.Calendar.MINUTE)
                        return String.format("%02d:%02d", hour, minute)
                    }
                    val filteredAntrian = dataAntrianMapped.filter { item ->
                        (item.status == 0 || item.status == 1) &&
                                item.waktu > getCurrentTimeString()
                    }


                    _antrian.value = filteredAntrian

                    if (filteredAntrian.isEmpty()) {
                        _message.value = "Belum ada antrian bro"
                    }

                } catch (e: Exception) {
                    _message.value = "Gagal memuat data"
                } finally {
                    _isRefreshingLoading.value = false
                }
            }
        }

    }