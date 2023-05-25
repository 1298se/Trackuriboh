package sam.g.trackuriboh.utils

import java.text.DateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * SHORT is completely numeric, such as 12.13.52 or 3:30pm
 * MEDIUM is longer, such as Jan 12, 1952
 * LONG is longer, such as January 12, 1952 or 3:30:32pm
 * FULL is pretty completely specified, such as Tuesday, April 12, 1952 AD or 3:30:42pm PST.
 */
fun Date?.formatDateTime(dateFormat: Int = DateFormat.MEDIUM, timeFormat: Int = DateFormat.SHORT) =
    this?.let {
        DateFormat.getDateTimeInstance(dateFormat, timeFormat).format(this).toString()
    }

fun Date?.formatDate(dateFormat: Int = DateFormat.MEDIUM) =
    this?.let {
        DateFormat.getDateInstance(
            dateFormat, Locale.getDefault()
        ).format(this).toString()
    }

/**
 * Adds the default TimeZone's current offset so that the day remains the same. We need this
 * because the MaterialDatePicker is in UTC.
 */
fun Date.fromLocalToUTC(): Date {
    val localTimeZone = TimeZone.getDefault()

    return Date(time + localTimeZone.getOffset(time))
}

fun Date.fromUTCToLocal(): Date {
    val localTimeZone = TimeZone.getDefault()

    return Date(time - localTimeZone.getOffset(time))
}

