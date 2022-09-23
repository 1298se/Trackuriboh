package sam.g.trackuriboh.utils

import java.util.*

const val DATABASE_PAGE_SIZE = 30

val DATABASE_ASSET_CREATION_DATE = with(Calendar.getInstance(TimeZone.getTimeZone("America/New_York"))) {
    // MONTH IS 0-indexed
    set(2022, 8, 20, 0, 0)
    timeInMillis
}
