package com.example.wakatime.data.remote

import com.example.wakatime.data.model.WakaUserSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WakaApi {
    @GET("/api/v1/users/current/summaries?start=today&end=today")
    suspend fun getWakaUserSummary(
        @Query("api_key") apiKey: String = "waka_30159012-1a44-427c-be48-5b39835409c8"
    ): WakaUserSummaryResponse
}