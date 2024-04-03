package com.example.wakatime.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.wakatime.data.reposiitory.WakaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class WakaRefreshWorker @AssistedInject constructor(
    private val wakaRepository: WakaRepository,
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork() =
        withContext(Dispatchers.IO) {
            wakaRepository.getWakaUserSummaryForWidget(applicationContext)
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
            workManager.enqueueUniqueWork(
                ONE_TIME_REFRESH_SYNC_WORKER,
                ExistingWorkPolicy.KEEP,
                oneTimeRefreshRequest
            )
        }
    }
}