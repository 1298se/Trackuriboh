package sam.g.trackuriboh.utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import sam.g.trackuriboh.R

const val MAX_PROGRESS = 100

fun createNotificationBuilder(
    context: Context,
    channelId: String,
    notificationTitle: String,
    channelName: String = context.getString(R.string.app_notification_channel_name),
    contentIntent: PendingIntent = getMainLauncherIntent(context.applicationContext),
    cancelIntent: PendingIntent? = null,
    showProgress: Boolean = false,
    ongoing: Boolean = false,
    autoCancel: Boolean = true,
): NotificationCompat.Builder {

    val builder = NotificationCompat.Builder(context.applicationContext, channelId)
        .setContentTitle(notificationTitle)
        .setTicker(notificationTitle)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentIntent(contentIntent)
        .setOngoing(ongoing)
        .setOnlyAlertOnce(true)
        .setAutoCancel(autoCancel)



    if (showProgress) {
        builder.setProgress(MAX_PROGRESS, 0, false)
    }

    if (cancelIntent != null) {
        builder.addAction(R.drawable.ic_baseline_close_24dp, context.getString(R.string.lbl_cancel), cancelIntent)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(context.applicationContext, channelId, channelName).also {
            builder.setChannelId(it.id)
        }
    }
    return builder
}

@TargetApi(Build.VERSION_CODES.O)
private fun createNotificationChannel(
    context: Context,
    channelId: String,
    name: String,
    notificationImportance: Int = NotificationManager.IMPORTANCE_HIGH
): NotificationChannel {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    return NotificationChannel(
        channelId, name, notificationImportance
    ).also { channel ->
        notificationManager.createNotificationChannel(channel)
    }
}