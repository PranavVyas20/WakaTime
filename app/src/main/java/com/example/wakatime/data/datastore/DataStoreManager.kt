package com.example.wakatime.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "WAKA_DATASTORE")
    private val WAKA_ACCESS_TOKEN = stringPreferencesKey("WAKA_ACCESS_TOKEN")
    private val WAKA_REFRESH_TOKEN = stringPreferencesKey("WAKA_REFRESH_TOKEN")
    private val WAKA_ACCESS_TOKEN_EXPIRES_AT = stringPreferencesKey("WAKA_ACCESS_TOKEN_EXPIRES_AT")

    suspend fun saveAuthTokenDataToDataStore(accessToken: String, refreshToken: String, expiryDate: String) {
        context.dataStore.edit { wakaDatastore ->
            wakaDatastore[WAKA_ACCESS_TOKEN] = accessToken
            wakaDatastore[WAKA_REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun removeAuthTokenDataFromDataStore() {
        context.dataStore.edit { wakaDataStore ->
            wakaDataStore.remove(WAKA_ACCESS_TOKEN)
            wakaDataStore.remove(WAKA_REFRESH_TOKEN)
        }
    }
    suspend fun getRefreshTokenFromDataStore(): String? =
        context.dataStore.data.first()[WAKA_REFRESH_TOKEN]

    suspend fun getAccessTokenFromDataStore(): String? =
        context.dataStore.data.first()[WAKA_ACCESS_TOKEN]

    suspend fun getAccessTokenExpiryDateFromDataStore(): String? =
        context.dataStore.data.first()[WAKA_ACCESS_TOKEN_EXPIRES_AT]
}
