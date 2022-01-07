package sam.g.trackuriboh.utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import sam.g.trackuriboh.R

const val MAX_PROGRESS = 100

const val DB_SYNC_PROGRESS_NOTIFICATION_ID = 1
// We need two separate IDs for progress and state because the progress notif will automatically disappear after the Work is done
// because it is provided in Worker.getForegroundInfo
const val DB_SYNC_STATE_NOTIFICATION_ID = 2

fun createNotificationBuilder(
    context: Context,
    notificationTitle: String,
    message: String?,
    channelId: String,
    channelName: String,
    contentIntent: PendingIntent = getMainLauncherIntent(context.applicationContext),
    showProgress: Boolean = false,
    ongoing: Boolean = false,
    autoCancel: Boolean = true,
): NotificationCompat.Builder {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(context.applicationContext, channelId, channelName)
    }
    val builder = NotificationCompat.Builder(context.applicationContext, channelId).apply {
        setContentTitle(notificationTitle)

        if (message != null) {
            setContentText(message)
        }

        setTicker(notificationTitle)
        setSmallIcon(R.drawable.ic_android)
        setContentIntent(contentIntent)
        setOngoing(ongoing)
        setOnlyAlertOnce(true)
        setAutoCancel(autoCancel)

        if (showProgress) {
            setProgress(MAX_PROGRESS, 0, false)
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
        channelId,
        name,
        notificationImportance
    ).also { channel ->
        channel.enableVibration(true)
        channel.enableLights(true)
        channel.lightColor = Color.WHITE

        notificationManager.createNotificationChannel(channel)
    }
}