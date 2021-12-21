package sam.g.trackuriboh.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationManagerCompat
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.Reminder
import sam.g.trackuriboh.utils.getPendingIntent
import sam.g.trackuriboh.workers.createNotificationBuilder

class ReminderBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val ARG_REMINDER = "Reminder"
        private const val REMINDER_NOTIFICATION_CHANNEL_ID = "TrackuribohReminder"
    }

    override fun onReceive(context: Context, intent: Intent) {
        intent.getParcelableExtra<Reminder>(ARG_REMINDER)?.let { reminder ->
            with(reminder) {
                val builder = createNotificationBuilder(
                    context = context,
                    channelId = REMINDER_NOTIFICATION_CHANNEL_ID,
                    notificationTitle = context.getString(
                        R.string.reminder_notification_title,
                        context.getString(type.resourceId)
                    ),
                    contentIntent = getPendingIntent(
                        context = context,
                        requestCode = 0,
                        intent = Intent(Intent.ACTION_VIEW, link?.let { Uri.parse(it) })
                    )
                )

                with(NotificationManagerCompat.from(context)) {
                    notify(id.toInt(), builder.build())
                }
            }

        }
    }
}