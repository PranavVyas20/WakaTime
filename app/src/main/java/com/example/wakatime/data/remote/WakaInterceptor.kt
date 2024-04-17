package com.example.wakatime.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider


class WakaInterceptor @Inject constructor(private val sessionManagerProvider: Provider<WakaSessionManager>) :
    Interceptor {
    private val sessionManager by lazy {
        sessionManagerProvider.get()
    }

    override fun intercept(chain: Interceptor.Chain) = runBlocking(Dispatchers.IO) {
        // if the request is for refresh token, skip intercepting
        val originalRequest = chain.request()
        if (chain.request().url.toString().contains("oauth/token")) {
            return@runBlocking chain.proceed(originalRequest)
        }
        val storedAccessToken = sessionManager.getAccessTokenFromDataStore()

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $storedAccessToken")
            .build()
        val response = chain.proceed(newRequest)

        // Auth token expired -> refetch the token -> save in datastore and make api call again
        if (response.code == 401) {
            return@runBlocking makeRefreshTokenCall(request = originalRequest, chain = chain)
        }
        return@runBlocking response
    }

    private suspend fun makeRefreshTokenCall(request: Request, chain: Interceptor.Chain): Response {
        val newAccessToken = sessionManager.getNewAuthTokenData()
        newAccessToken?.let {
            sessionManager.saveAuthTokenDataToDataStore(it)
            val newRequest = request.newBuilder()
                .header("Authorization", "Bearer ${newAccessToken.accessToken}")
                .build()
            return chain.proceed(newRequest)
        } ?: kotlin.run {
            // incase the refresh token is also expired, then logout the user
            sessionManager.logoutUser()
            return chain.proceed(request)
        }
    }
}