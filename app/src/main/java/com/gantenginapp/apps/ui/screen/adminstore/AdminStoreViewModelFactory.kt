package com.gantenginapp.apps.ui.screen.adminstore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.gantenginapp.apps.data.repository.*
import com.gantenginapp.apps.ui.screen.adminstore.AdminStoreViewModel

class AdminStoreViewModelFactory(
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminStoreViewModel::class.java)) {
            return AdminStoreViewModel( storeRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
