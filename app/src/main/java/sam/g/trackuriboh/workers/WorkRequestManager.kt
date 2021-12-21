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
            DatabaseDownloadWorker.WORKER_NAME,
            ExistingWorkPolicy.REPLACE,
            databaseDownloadRequest
        )
    }

    fun enqueueDatabaseUpdateWorker(updateCardSetIds: LongArray?) {
        val databaseUpdateRequest = OneTimeWorkRequestBuilder<DatabaseUpdateWorker>()
            .setInputData(workDataOf(DatabaseUpdateWorker.CARD_SET_IDS_INPUT_KEY to updateCardSetIds))
            .build()

        workManager.enqueueUniqueWork(
            DatabaseUpdateWorker.WORKER_NAME,
            ExistingWorkPolicy.REPLACE,
            databaseUpdateRequest
        )
    }

    fun enqueueDatabaseUpdateCheck(isUserTriggered: Boolean) {
        val databaseUpdateRequest = OneTimeWorkRequestBuilder<DatabaseUpdateCheckWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            if (isUserTriggered) DatabaseUpdateCheckWorker.USER_TRIGGERED_WORKER_NAME else DatabaseUpdateCheckWorker.BACKGROUND_WORKER_NAME,
            ExistingWorkPolicy.REPLACE,
            databaseUpdateRequest
        )
    }

    fun enqueuePeriodicDatabaseUpdateCheck() {
        // Check for database updates on the background
        val databaseUpdateScheduleRequest = PeriodicWorkRequestBuilder<DatabaseUpdateCheckScheduleWorker>(24, TimeUnit.HOURS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            DatabaseUpdateCheckScheduleWorker.WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            databaseUpdateScheduleRequest
        )
    }
}