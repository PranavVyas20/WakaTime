package com.example.wakatime.workmanager

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.wakatime.data.model.toWakaUserSummaryData
import com.example.wakatime.data.reposiitory.WakaRepository
import com.example.wakatime.ui.widgets.glance.WakaGlanceStateDefinition
import com.example.wakatime.ui.widgets.glance.WakaGlanceWidget
import com.example.wakatime.ui.widgets.glance.WakaUserSummaryResponseState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WakaRefreshWorker @AssistedInject constructor(
    private val wakaRepository: WakaRepository,
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        WakaGlanceStateDefinition
            .getDataStore(applicationContext, WakaGlanceStateDefinition.fileName)
            .updateData {
                WakaUserSummaryResponseState.IsLoading
            }
        WakaGlanceWidget.updateAll(applicationContext)
        wakaRepository.getWakaUserSummary()
            .onSuccess {data ->
                Log.d("waka_tag", "$data")
                WakaGlanceStateDefinition
                    .getDataStore(applicationContext, WakaGlanceStateDefinition.fileName)
                    .updateData {
                        WakaUserSummaryResponseState.Success(data.toWakaUserSummaryData())
                    }
                WakaGlanceWidget.updateAll(applicationContext)
                return Result.success()
            }
            .onFailure {error ->
                Log.d("waka_tag", "${error.message}")
                WakaGlanceStateDefinition
                    .getDataStore(applicationContext, WakaGlanceStateDefinition.fileName)
                    .updateData {
                        WakaUserSummaryResponseState.Error(error.message)
                    }
                WakaGlanceWidget.updateAll(applicationContext)
            }
        return Result.failure()
    }

    companion object {
        private const val ONE_TIME_REFRESH_SYNC_WORKER = "ONE_TIME_REFRESH_SYNC_WORKER"

        private val oneTimeRefreshConstraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        private val oneTimeRefreshRequest = OneTimeWorkRequestBuilder<WakaRefreshWorker>()
            .setConstraints(oneTimeRefreshConstraints)
            .build()

        fun start(context: Context) {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniqueWork(ONE_TIME_REFRESH_SYNC_WORKER, ExistingWorkPolicy.KEEP, oneTimeRefreshRequest)
        }
    }
}