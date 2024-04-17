package com.example.wakatime.data.remote

import com.example.wakatime.data.model.WakaAuthTokenData
import com.example.wakatime.data.reposiitory.WakaRepository
import javax.inject.Inject

class WakaSessionManager @Inject constructor(private val wakaRepository: WakaRepository) {
    suspend fun saveAuthTokenDataToDataStore(data: WakaAuthTokenData) {
        wakaRepository.saveAuthTokenDataInDataStore(data)
    }
    suspend fun getNewAuthTokenData(): WakaAuthTokenData? {
         return wakaRepository.getAuthTokenData().getOrNull()
    }
    suspend fun logoutUser() {
        wakaRepository.logoutUser()
    }
    suspend fun getAccessTokenFromDataStore() = wakaRepository.getAccessTokenFromDataStore()
}