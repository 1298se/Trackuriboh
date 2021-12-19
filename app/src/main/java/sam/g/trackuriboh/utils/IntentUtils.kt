package sam.g.trackuriboh.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import sam.g.trackuriboh.MainActivity

fun getMainLauncherIntent(context: Context): PendingIntent =
    getPendingIntent(
        context = context,
        intent = Intent(context.applicationContext, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    )

fun getPendingIntent(
    context: Context,
    requestCode: Int = 0,
    intent: Intent,
    flags: Int = getPendingIntentFlags()
): PendingIntent =
    PendingIntent.getActivity(
        context.applicationContext,
        0,
        intent,
        getPendingIntentFlags()
    )

fun getPendingIntentFlags(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
}