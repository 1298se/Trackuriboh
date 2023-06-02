package sam.g.trackuriboh.workers

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkRequestManager @Inject constructor(
    private val workManager: WorkManager
) {
    fun enqueueDatabaseDownloadWorker() {
        val databaseDownloadRequest = OneTimeWorkRequestBuilder<DatabaseDownloadWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build()
            )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            DatabaseDownloadWorker.workerName,
            ExistingWorkPolicy.REPLACE,
            databaseDownloadRequest
        )
    }

    fun enqueueDatabaseUpdateWorker(updateCardSetIds: LongArray?) {
        val databaseUpdateRequest = OneTimeWorkRequestBuilder<DatabaseUpdateWorker>()
            .setInputData(workDataOf(DatabaseUpdateWorker.CARD_SET_IDS_INPUT_KEY to updateCardSetIds))
            .build()

        workManager.enqueueUniqueWork(
            DatabaseUpdateWorker.workerName,
            ExistingWorkPolicy.REPLACE,
            databaseUpdateRequest
        )
    }

    fun enqueueDatabaseUpdateCheck() {
        val databaseUpdateRequest = OneTimeWorkRequestBuilder<DatabaseUpdateCheckWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            DatabaseUpdateCheckWorker.workerName,
            ExistingWorkPolicy.REPLACE,
            databaseUpdateRequest
        )
    }

    /**
     * Schedules a worker to periodically schedule database update checks
     */
    fun enqueuePeriodicDatabaseUpdateCheckScheduler() {
        val databaseUpdateScheduleRequest = PeriodicWorkRequestBuilder<DatabaseUpdateCheckScheduleWorker>(24, TimeUnit.HOURS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            DatabaseUpdateCheckScheduleWorker.workerName,
            ExistingPeriodicWorkPolicy.KEEP,
            databaseUpdateScheduleRequest
        )
    }

    fun enqueueOneTimePriceSync() {
        val priceSyncRequest = OneTimeWorkRequestBuilder<PriceSyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()

        workManager.enqueueUniqueWork(
            PriceSyncWorker.workerName,
            ExistingWorkPolicy.REPLACE,
            priceSyncRequest
        )
    }
}
