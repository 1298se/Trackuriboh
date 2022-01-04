package sam.g.trackuriboh.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.data.repository.ReminderRepository
import sam.g.trackuriboh.receivers.ReminderBroadcastReceiver
import sam.g.trackuriboh.utils.getMainLauncherIntent
import sam.g.trackuriboh.utils.getPendingIntentFlags
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    private val alarmManager: AlarmManager,
    private val reminderRepository: ReminderRepository,
    @ApplicationContext private val applicationContext: Context
) {
    fun canScheduleReminders(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    /*
     * If the stated trigger time is in the past, the alarm will be triggered immediately. If there is already an alarm for this
     * intent scheduled, it will be removed and replaced by this one.
     * See https://developer.android.com/reference/android/app/AlarmManager#set(int,%20long,%20android.app.PendingIntent)
     */
    fun scheduleReminder(reminder: Reminder) {
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(reminder.date.time, getMainLauncherIntent(applicationContext)),
            getReminderOperation(reminder)
        )
    }

    suspend fun schedulePendingReminders() {
        withContext(Dispatchers.IO) {
            if (!canScheduleReminders()) {
                return@withContext
            }

            val reminders = reminderRepository.getReminders()

            reminders.forEach {
                if (!it.notificationDisplayed) {
                    scheduleReminder(it)
                }
            }
        }
    }

    private fun getReminderOperation(reminder: Reminder): PendingIntent {
        val intent = Intent(applicationContext, ReminderBroadcastReceiver::class.java).apply {
            action = ReminderBroadcastReceiver.ACTION_SET_REMINDER
            putExtra(ReminderBroadcastReceiver.ARG_REMINDER, reminder)
        }

        return PendingIntent.getBroadcast(
            applicationContext,
            reminder.id.toInt(),
            intent,
            getPendingIntentFlags()
        )
    }


}