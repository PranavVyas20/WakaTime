package com.example.wakatime.data.remote

import com.example.wakatime.data.model.WakaUserSummaryResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WakaApi {

    @GET("/api/v1/users/current/summaries?start=today&end=today")
    suspend fun getWakaUserSummary(
        @Query("scope") scope: String = "read_summaries",
    ): WakaUserSummaryResponse

    @FormUrlEncoded
    @POST("/oauth/token")
    suspend fun getAuthToken(
        @Field("client_id") clientId: String = "IYe2gJjSAF0A388BRHIlZP2O",
        @Field("scope") scope: String = "read_summaries",
        @Field("client_secret") clientSecret: String = "waka_sec_DK6NdyxCB36T5tDJgdJ4YkROJJtCrVntvkZ9snfx0brmXtUNdVkNKKuARVbM3yNWmS4FJt8uM0yLJoll",
        @Field("redirect_uri") redirectUri: String = "https://www.youtube.com/",
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String = "waka_ref_bCTl2uugbjikmFnf4wylNFY5yIgqJAOl0rQd7ENL0CPaqzhwtgpzWBBIdg91iPnzTNBchbymDpZQUd0X"
    ): String
}