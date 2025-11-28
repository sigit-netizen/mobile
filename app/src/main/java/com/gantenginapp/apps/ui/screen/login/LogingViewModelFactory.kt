package com.gantenginapp.apps.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gantenginapp.apps.data.repository.AuthRepositoryImpl
import  android.content.Context
import com.gantenginapp.apps.data.local.UserPreferences

class LoginViewModelFactory(
    private val repository: AuthRepositoryImpl,
    private val context: Context,
    private val prefs: UserPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository, context, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}