package com.example.wakatime.data.reposiitory

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.work.ListenableWorker
import com.example.wakatime.data.datastore.DataStoreManager
import com.example.wakatime.data.model.WakaAuthTokenData
import com.example.wakatime.data.model.WakaUserSummaryResponse
import com.example.wakatime.data.model.toWakaUserSummaryData
import com.example.wakatime.data.remote.WakaApi
import com.example.wakatime.ui.widgets.glance.WakaGlanceStateDefinition
import com.example.wakatime.ui.widgets.glance.WakaGlanceWidget
import com.example.wakatime.ui.widgets.glance.WakaUserSummaryResponseState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WakaRepository @Inject constructor(private val dataStoreManager: DataStoreManager, private val wakaApi: WakaApi) {

    suspend fun getAccessTokenFromDataStore() = dataStoreManager.getAccessTokenFromDataStore()
    suspend fun getRefreshTokenFromDataStore() = dataStoreManager.getRefreshTokenFromDataStore()
    suspend fun getAccessTokenExpiryDateFromDataStore() = dataStoreManager.getAccessTokenExpiryDateFromDataStore()
    suspend fun getWakaUserSummary(): Result<WakaUserSummaryResponse> {
        return runCatching {
            wakaApi.getWakaUserSummary()
        }
    }

    suspend fun saveAuthTokenDataInDataStore(data: WakaAuthTokenData) {
        dataStoreManager.saveAuthTokenDataToDataStore(accessToken = data.accessToken, refreshToken = data.refreshToken, expiryDate = data.expiresAt)
    }

    /**
     * sample : ("access_token=waka_tok_kTbUUsOVt8y3mKLADPamR9qYThLFYe0wiJ7wRE0LSBtLTHWjUHM1jpS7oGlLlgKtoGpn50nC4nSiZxgr" +
     *           "&refresh_token=waka_ref_fOHbZzwGyIlE8qG0EoL1eJqrPNTsIxl3nnYb9QuU6KlSiPcLzLLWxCY9rf3MJVB7T9JqnJHPos4ArGIz&uid=e455d92f-0542-4e6f-9a59-26a7cd892d81" +
     *           "&token_type=bearer&expires_at=2025-04-08T13%3A31%3A08Z&expires_in=31536000&scope=")
     **/
     fun extractTokens(refreshTokenResponse: String): WakaAuthTokenData {
        val map = hashMapOf<String, String>()
        refreshTokenResponse.split("&").onEach { pair ->
            val key = pair.split("=")[0]
            val value = pair.split("=")[1]
            map[key] = value
        }
        return WakaAuthTokenData(
            accessToken = map["access_token"]!!,
            refreshToken = map["refresh_token"]!!,
            expiresAt = map["expires_at"]!!
        )
    }

    suspend fun getAuthTokenData(): Result<WakaAuthTokenData> = runCatching {
        wakaApi.getAuthToken().let(::extractTokens)
    }


    suspend fun getWakaUserSummaryForWidget(context: Context): ListenableWorker.Result {
        WakaGlanceStateDefinition
            .getDataStore(context, WakaGlanceStateDefinition.fileName)
            .updateData {
                WakaUserSummaryResponseState.IsLoading
            }
        WakaGlanceWidget.updateAll(context)
        getWakaUserSummary()
            .onSuccess { data ->
                Log.d("waka_tag", "$data")
                WakaGlanceStateDefinition
                    .getDataStore(context, WakaGlanceStateDefinition.fileName)
                    .updateData {
                        WakaUserSummaryResponseState.Success(data.toWakaUserSummaryData())
                    }
                WakaGlanceWidget.updateAll(context)
                return ListenableWorker.Result.success()
            }
            .onFailure { error ->
                Log.d("waka_tag", "${error.message}")
                WakaGlanceStateDefinition
                    .getDataStore(context, WakaGlanceStateDefinition.fileName)
                    .updateData {
                        WakaUserSummaryResponseState.Error(error.message)
                    }
                WakaGlanceWidget.updateAll(context)
            }
        return ListenableWorker.Result.failure()
    }
}