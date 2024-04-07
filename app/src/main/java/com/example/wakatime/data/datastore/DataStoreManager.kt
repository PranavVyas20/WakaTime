package com.example.wakatime.data.datastore

import android.content.Context
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "WAKA_DATASTORE")
    companion object {
        val WAKA_ACCESS_TOKEN = stringPreferencesKey("WAKA_ACCESS_TOKEN")
        val WAKA_REFRESH_TOKEN = stringPreferencesKey("WAKA_REFRESH_TOKEN")
    }

    suspend fun saveToDataStore(accessToken: String, refreshToken: String) {
        context.dataStore.edit { wakaDatastore ->
         wakaDatastore[WAKA_ACCESS_TOKEN] = accessToken
         wakaDatastore[WAKA_REFRESH_TOKEN] = refreshToken
        }
    }
    suspend fun getFromDataStore():String? = context.dataStore.data.first()[WAKA_ACCESS_TOKEN]
}
