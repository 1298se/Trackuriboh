package sam.g.trackuriboh.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Just a worker that starts a [DatabaseUpdateWorker].
 * Since we cannot get output data from PeriodicWorkRequests, we create a PeriodicWorkRequest
 * for this worker that schedules a OneTimeWorkRequest for the actual check.
 */
@HiltWorker
class DatabaseUpdateCheckScheduleWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val workRequestManager: WorkRequestManager,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        val workerName = DatabaseUpdateCheckScheduleWorker::class.java.name
    }

    override suspend fun doWork(): Result {

        workRequestManager.enqueueDatabaseUpdateCheck(false)

        return Result.success()
    }
}