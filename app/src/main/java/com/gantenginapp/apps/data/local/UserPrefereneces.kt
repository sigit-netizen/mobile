package com.gantenginapp.apps.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gantenginapp.apps.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {

        private val KEY_USERID = stringPreferencesKey("userid")
        private val KEY_USERNAME = stringPreferencesKey("username")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_PHONE = stringPreferencesKey("phone")
        private val KEY_ROLE = stringPreferencesKey("role")
        private val KEY_PASSWORD = stringPreferencesKey("password")
    }

    val userFlow: Flow<User> = context.dataStore.data
        .map { prefs ->
            User(
                id = prefs[KEY_USERID] ?: "",
                username = prefs[KEY_USERNAME] ?: "",
                email = prefs[KEY_EMAIL] ?: "",
                noHp = prefs[KEY_PHONE] ?: "",
                role = prefs[KEY_ROLE] ?: "",
            )
        }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USERID] = user.id
            prefs[KEY_USERNAME] = user.username
            prefs[KEY_EMAIL] = user.email
            prefs[KEY_PHONE] = user.noHp
            prefs[KEY_ROLE] = user.role

        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { it.clear()}
    }

    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_USERID]
        }
    }

}