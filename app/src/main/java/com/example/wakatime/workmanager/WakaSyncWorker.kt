package com.example.wakatime.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.wakatime.data.reposiitory.WakaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class WakaSyncWorker @AssistedInject constructor(
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
        private const val periodicSync = "PeriodicSyncWorker"

        private val periodicSyncConstraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        private val periodicSyncRequest = PeriodicWorkRequestBuilder<WakaSyncWorker>(repeatInterval = 15, TimeUnit.MINUTES)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setConstraints(periodicSyncConstraints)
            .build()

        fun start(context: Context) {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniquePeriodicWork(periodicSync, ExistingPeriodicWorkPolicy.KEEP, periodicSyncRequest)
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(periodicSync)
        }
    }
}