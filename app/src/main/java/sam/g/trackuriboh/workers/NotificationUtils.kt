package sam.g.trackuriboh.workers

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import sam.g.trackuriboh.MainActivity
import sam.g.trackuriboh.R

const val MAX_PROGRESS = 100

private const val CHANNEL_ID = "TrackuribohDBSync"

fun ListenableWorker.createNotificationBuilder(
    channelId: String = CHANNEL_ID,
    channelName: String,
    notificationTitle: String,
    cancelText: String = applicationContext.getString(R.string.lbl_cancel),
    showProgress: Boolean = false,
    contentIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        Intent(applicationContext, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        },
        PendingIntent.FLAG_IMMUTABLE
    ),
): NotificationCompat.Builder {

    val cancelIntent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)

    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setContentTitle(notificationTitle)
        .setTicker(notificationTitle)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentIntent(contentIntent)
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .addAction(R.drawable.ic_close_black_24dp, cancelText, cancelIntent)

    if (showProgress) {
        builder.setProgress(MAX_PROGRESS, 0, false)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(applicationContext, channelId, channelName).also {
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