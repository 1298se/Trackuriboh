package sam.g.trackuriboh.utils

import java.util.*

const val DATABASE_PAGE_SIZE = 30

val DATABASE_ASSET_CREATION_DATE = with(Calendar.getInstance(TimeZone.getTimeZone("America/New_York"))) {
    set(2022, 9, 3, 0, 0, 0)
    timeInMillis
}
