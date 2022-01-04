package sam.g.trackuriboh.receivers

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.data.repository.ReminderRepository
import sam.g.trackuriboh.managers.ReminderScheduler
import sam.g.trackuriboh.utils.createNotificationBuilder
import sam.g.trackuriboh.utils.getOpenLinkIntent
import sam.g.trackuriboh.utils.getPendingIntent
import sam.g.trackuriboh.utils.goAsync
import sam.g.trackuriboh.workers.WorkRequestManager
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var workRequestManager: WorkRequestManager
    @Inject lateinit var reminderScheduler: ReminderScheduler
    @Inject lateinit var reminderRepository: ReminderRepository
    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        const val ARG_REMINDER = "ReminderBroadcastReceiver_argReminder"
        const val ACTION_SET_REMINDER = "sam.g.trackuriboh.SET_REMINDER"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED, AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                firebaseAnalytics.logEvent(Events.REMINDER_SCHEDULED, bundleOf("action" to intent.action))

                goAsync {
                    reminderScheduler.schedulePendingReminders()
                }
            }
            ACTION_SET_REMINDER -> {
                intent.getParcelableExtra<Reminder>(ARG_REMINDER)?.let { reminder ->

                    // Update that we've shown the notification
                    goAsync {
                        reminderRepository.updateReminder(reminder.copy(notificationDisplayed = true))

                        val reminderType = context.getString(reminder.type.resourceId)

                        val reminderHostTypeString = if (!reminder.host.isNullOrEmpty()) {
                            context.getString(R.string.reminder_title, reminder.host, reminderType)
                        } else {
                            reminderType
                        }

                        // We don't need to worry about ActivityNotFoundException here because it's a PendingIntent
                        val builder = createNotificationBuilder(
                            context = context,
                            notificationTitle = context.getString(R.string.reminder_notification_title),
                            message = context.getString(R.string.reminder_notification_message, reminderHostTypeString),
                            channelId = context.getString(R.string.reminder_notification_channel_id),
                            channelName = context.getString(R.string.reminder_notification_channel_name),
                            contentIntent = getPendingIntent(
                                context = context,
                                intent = getOpenLinkIntent(reminder.link)
                            )
                        )

                        with(NotificationManagerCompat.from(context)) {
                            notify(System.currentTimeMillis().toInt(), builder.build())
                        }

                        firebaseAnalytics.logEvent(Events.REMINDER_NOTIFICATION_DISPLAYED, bundleOf("reminder" to reminder))
                    }
                }
            }
        }
    }
}
