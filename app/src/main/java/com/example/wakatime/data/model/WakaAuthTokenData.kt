package com.example.wakatime.data.model

data class WakaAuthTokenData(
    var accessToken: String,
    var refreshToken: String,
    var expiresAt: String,
)
