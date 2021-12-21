package sam.g.trackuriboh.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.receivers.ReminderBroadcastReceiver
import sam.g.trackuriboh.utils.getMainLauncherIntent
import sam.g.trackuriboh.utils.getPendingIntentFlags
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val applicationContext: Context
) {
    fun canScheduleReminders(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun setReminder(reminder: Reminder) {
        val intent = Intent(applicationContext, ReminderBroadcastReceiver::class.java).apply {
            putExtra(ReminderBroadcastReceiver.ARG_REMINDER, reminder)
        }

        val operation = PendingIntent.getBroadcast(
            applicationContext,
            reminder.id.toInt(),
            intent,
            getPendingIntentFlags()
        )

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(reminder.date.time, getMainLauncherIntent(applicationContext)),
            operation
        )
    }
}