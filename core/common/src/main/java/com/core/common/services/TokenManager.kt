package com.core.common.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN_KEY]
    }

    suspend fun getFormattedToken(): String {
        return "Bearer ${token.first()}"
    }

    suspend fun getFormattedTokenOrEmpty():String {
        return getFormattedToken().takeIf { containsJwtToken() } ?: ""
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }

    /**
     * Checks if token is stored in datastore
     */
    suspend fun containsJwtToken(): Boolean {
        return token.map { it != null }.firstOrNull() ?: false
    }

    /**
     * Check if in JWT-token format
     */
    private fun verifyJwtToken(token: String): Boolean {
        val parts = token.split(".")
        return parts.size == 3 && parts.all { isBase64Encoded(it) }
    }

    /**
     * Check if string is Base64 encoded
     */
    private fun isBase64Encoded(value: String): Boolean {
        return try {
            android.util.Base64.decode(value, android.util.Base64.URL_SAFE)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}

//get DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")