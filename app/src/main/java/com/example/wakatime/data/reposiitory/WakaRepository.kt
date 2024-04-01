package com.example.wakatime.data.reposiitory

import com.example.wakatime.data.model.WakaUserSummaryResponse
import com.example.wakatime.data.remote.WakaApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WakaRepository @Inject constructor(private val wakaApi: WakaApi) {
   suspend fun getWakaUserSummary(): Result<WakaUserSummaryResponse> {
        return runCatching {
            wakaApi.getWakaUserSummary()
        }
    }
}