package com.example.wakatime.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import javax.inject.Inject
import javax.inject.Provider


class WakaInterceptor @Inject constructor(private val sessionManagerProvider: Provider<WakaSessionManager>) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain) = runBlocking {
        // if the request is for refresh token, skip all the intercepting
        val originalRequest = chain.request()
        if (chain.request().url.toString() == "oauth/token") {
            return@runBlocking chain.proceed(originalRequest)
        }

        val isAccessTokenExpired =
            sessionManagerProvider.get().isAccessTokenExpired()

        if (isAccessTokenExpired) {
            val newAccessToken = sessionManagerProvider.get().getNewAuthTokenData()
            newAccessToken?.let {
                sessionManagerProvider.get().saveAuthTokenDataToDataStore(it)
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer ${newAccessToken.accessToken}")
                    .build()
                chain.proceed(newRequest)
            }
        }
        val savedAccessToken =
            sessionManagerProvider.get().getAccessTokenFromDataStore()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $savedAccessToken")
            .build()
        chain.proceed(newRequest)
    }
}