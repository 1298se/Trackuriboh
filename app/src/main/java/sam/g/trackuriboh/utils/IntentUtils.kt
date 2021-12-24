package sam.g.trackuriboh.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
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

fun getOpenLinkIntent(url: String): Intent =
    Intent(Intent.ACTION_VIEW, Uri.parse(url))

fun getPendingIntent(
    context: Context,
    requestCode: Int = 0,
    intent: Intent,
    flags: Int = getPendingIntentFlags()
): PendingIntent =
    PendingIntent.getActivity(
        context.applicationContext,
        requestCode,
        intent,
        flags,
    )

fun getPendingIntentFlags(): Int {
    return PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
}