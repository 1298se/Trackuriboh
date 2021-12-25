package sam.g.trackuriboh.receivers

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.utils.createNotificationBuilder
import sam.g.trackuriboh.utils.getOpenLinkIntent
import sam.g.trackuriboh.utils.getPendingIntent
import sam.g.trackuriboh.workers.WorkRequestManager
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var workRequestManager: WorkRequestManager

    companion object {
        const val ARG_REMINDER = "Reminder"
        const val ACTION_SET_REMINDER = "sam.g.trackuriboh.SET_REMINDER"

        private const val REMINDER_NOTIFICATION_CHANNEL_ID = "TrackuribohReminder"
    }

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED, AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                workRequestManager.enqueueReminderScheduler()
            }
            ACTION_SET_REMINDER -> {
                intent.getParcelableExtra<Reminder>(ARG_REMINDER)?.let { reminder ->

                    val reminderType = context.getString(reminder.type.resourceId)

                    val reminderTitle = if (!reminder.host.isNullOrEmpty()) {
                        context.getString(R.string.reminder_title, reminder.host, reminderType)
                    } else {
                        reminderType
                    }

                    // We don't need to worry about ActivityNotFoundException here because it's a PendingIntent
                    with(reminder) {
                        val builder = createNotificationBuilder(
                            context = context,
                            channelId = REMINDER_NOTIFICATION_CHANNEL_ID,
                            notificationTitle = context.getString(
                                R.string.reminder_notification_title,
                                reminderTitle
                            ),
                            contentIntent = getPendingIntent(
                                context = context,
                                requestCode = 0,
                                intent = getOpenLinkIntent(reminder.link)
                            )
                        )

                        with(NotificationManagerCompat.from(context)) {
                            notify(id.toInt(), builder.build())
                        }
                    }
                }
            }
        }
    }
}