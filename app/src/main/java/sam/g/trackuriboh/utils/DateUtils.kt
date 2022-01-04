package sam.g.trackuriboh.utils

import java.text.DateFormat
import java.util.*

fun formatReminderDateTime(date: Date) =
    DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(date)