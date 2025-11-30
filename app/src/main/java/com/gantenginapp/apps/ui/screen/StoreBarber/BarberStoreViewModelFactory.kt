package com.gantenginapp.apps.ui.screen.StoreBarber
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.gantenginapp.apps.data.repository.*
class BarberStoreViewModelFactory(
    private val storeId: Int,
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BarberStoreViewModel(storeRepository, storeId, userRepository) as T
    }
}
