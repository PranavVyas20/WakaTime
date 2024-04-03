package com.example.wakatime.data.reposiitory

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.work.ListenableWorker
import com.example.wakatime.data.model.WakaUserSummaryResponse
import com.example.wakatime.data.model.toWakaUserSummaryData
import com.example.wakatime.data.remote.WakaApi
import com.example.wakatime.ui.widgets.glance.WakaGlanceStateDefinition
import com.example.wakatime.ui.widgets.glance.WakaGlanceWidget
import com.example.wakatime.ui.widgets.glance.WakaUserSummaryResponseState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WakaRepository @Inject constructor(private val wakaApi: WakaApi) {
   suspend fun getWakaUserSummary(): Result<WakaUserSummaryResponse> {
        return runCatching {
            wakaApi.getWakaUserSummary()
        }
    }

    suspend fun getWakaUserSummaryForWidget(context: Context): ListenableWorker.Result {
        WakaGlanceStateDefinition
            .getDataStore(context, WakaGlanceStateDefinition.fileName)
            .updateData {
                WakaUserSummaryResponseState.IsLoading
            }
        WakaGlanceWidget.updateAll(context)
        getWakaUserSummary()
            .onSuccess {data ->
                Log.d("waka_tag", "$data")
                WakaGlanceStateDefinition
                    .getDataStore(context, WakaGlanceStateDefinition.fileName)
                    .updateData {
                        WakaUserSummaryResponseState.Success(data.toWakaUserSummaryData())
                    }
                WakaGlanceWidget.updateAll(context)
                return ListenableWorker.Result.success()
            }
            .onFailure {error ->
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