package com.example.wakatime.data.remote

import com.example.wakatime.data.model.WakaUserSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WakaApi {
    @GET("/api/v1/users/current/summaries?start=today&end=today")
    suspend fun getWakaUserSummary(
        @Query("api_key") apiKey: String = "waka_30159012-1a44-427c-be48-5b39835409c8"
    ): WakaUserSummaryResponse

    @GET("/oauth/authorize")
    suspend fun authorizeUserAccount()

    @FormUrlEncoded
    @POST("/oauth/token")
    suspend fun getAuthToken(
        @Field("client_id") clientId: String = "IYe2gJjSAF0A388BRHIlZP2O",
        @Field("client_secret") clientSecret: String = "waka_sec_TMTLUVTlBnyFTppzpauScbmzMJa9x6TvgSkBFQzbKzRPCGSoY84myMmGq1tFFAyIb8pRCcgvCkyCf39w",
        @Field("redirect_uri") redirectUri: String = "https://www.youtube.com/",
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String = "waka_ref_bCTl2uugbjikmFnf4wylNFY5yIgqJAOl0rQd7ENL0CPaqzhwtgpzWBBIdg91iPnzTNBchbymDpZQUd0X"
    ): String
}