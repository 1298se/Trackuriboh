package sam.g.trackuriboh.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.repository.ReminderRepository
import sam.g.trackuriboh.managers.ReminderScheduler
import sam.g.trackuriboh.utils.createNotificationBuilder
import java.util.*

/**
 * Worker that checks all created reminders and resets the alarm for them if they haven't expired yet.
 */
@HiltWorker
class ReminderScheduleWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler,
    private val workManager: WorkManager,
    ) : CoroutineWorker(appContext, workerParams) {

    private val progressNotificationBuilder by lazy {
        createNotificationBuilder(
            context = applicationContext,
            channelId = DB_SYNC_NOTIFICATION_CHANNEL_ID,
            notificationTitle = appContext.getString(R.string.database_download_worker_notification_title),
        )
    }


    override suspend fun doWork(): Result {
        val reminders = reminderRepository.getReminders()

        val curDate = Date()
        reminders.forEach {
            if (it.date >= curDate) {
                reminderScheduler.setReminder(it)
            }
        }

        return Result.success()
    }
}