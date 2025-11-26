package com.gantenginapp.apps.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.gantenginapp.apps.domain.model.User

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("username")
        private val USER_EMAIL = stringPreferencesKey("email")
        private val USER_NO_HP = stringPreferencesKey("no_hp")
        private val USER_ROLE = stringPreferencesKey("role")
    }

    // SAVE FULL USER
    suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = user.id ?: "_]()
