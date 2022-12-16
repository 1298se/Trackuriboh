package sam.g.trackuriboh.utils

import java.text.DateFormat
import java.util.*

fun Date?.formatDateTime(dateFormat: Int, timeFormat: Int) =
    this?.let {
        DateFormat.getDateTimeInstance(dateFormat, timeFormat).format(this).toString()
    }

fun Date?.formatDate(dateFormat: Int) =
    this?.let {
        DateFormat.getDateInstance(
            dateFormat, Locale.getDefault()
        ).format(this).toString()
    }