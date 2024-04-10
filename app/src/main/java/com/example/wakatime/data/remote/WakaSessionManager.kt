package com.example.wakatime.data.remote

import android.annotation.SuppressLint
import com.example.wakatime.data.model.WakaAuthTokenData
import com.example.wakatime.data.reposiitory.WakaRepository
import dagger.Provides
import java.time.Instant
import javax.inject.Inject

class WakaSessionManager @Inject constructor(private val wakaRepository: WakaRepository) {
    @SuppressLint("NewApi")
    suspend fun isAccessTokenExpired(): Boolean {
        val storedAccessToken = wakaRepository.getAccessTokenFromDataStore()
        storedAccessToken?.let {
            val expiresAtInstant = Instant.parse(storedAccessToken)
            val currentTime = Instant.now()
            return currentTime.isAfter(expiresAtInstant)
        }?: kotlin.run {
            return true
        }
    }

    suspend fun saveAuthTokenDataToDataStore(data: WakaAuthTokenData) {
        wakaRepository.saveAuthTokenDataInDataStore(data)
    }
    suspend fun getNewAuthTokenData(): WakaAuthTokenData? {
         return wakaRepository.getAuthTokenData().getOrNull()
    }
    suspend fun getAccessTokenFromDataStore() = wakaRepository.getAccessTokenFromDataStore()
}