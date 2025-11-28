package com.gantenginapp.apps.ui.screen.home
import com.gantenginapp.apps.data.repository.UserRepository
import com.gantenginapp.apps.ui.screen.home.HomeViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gantenginapp.apps.data.repository.DataRefresher
import com.gantenginapp.apps.data.repository.StoreRepository


class HomeViewModelFactory(
    private val userRepository: UserRepository,
    private val dataRefresher: DataRefresher,
    private val storeRepository: StoreRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(userRepository, dataRefresher,storeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
