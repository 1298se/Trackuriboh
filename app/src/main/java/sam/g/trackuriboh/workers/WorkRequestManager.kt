package sam.g.trackuriboh.workers

import androidx.work.*
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

    fun enqueuePeriodicPriceSync() {
        val priceSyncRequest = PeriodicWorkRequestBuilder<PriceSyncWorker>(3, TimeUnit.DAYS)
            .setInitialDelay(10, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            PriceSyncWorker::class.java.name,
            ExistingPeriodicWorkPolicy.KEEP,
            priceSyncRequest
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
            PriceSyncWorker::class.java.name + "OneTime",
            ExistingWorkPolicy.REPLACE,
            priceSyncRequest
        )
    }
}
