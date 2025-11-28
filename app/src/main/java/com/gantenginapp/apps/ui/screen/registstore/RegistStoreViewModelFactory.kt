package com.gantenginapp.apps.ui.screen.registstore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gantenginapp.apps.data.repository.StoreRepository
import com.gantenginapp.apps.ui.screen.registstore.RegisterStoreViewModel
import com.gantenginapp.apps.data.repository.UserRepository


class RegistStoreViewModelFactory(
    private val repository: StoreRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterStoreViewModel::class.java)) {
            return RegisterStoreViewModel(repository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
