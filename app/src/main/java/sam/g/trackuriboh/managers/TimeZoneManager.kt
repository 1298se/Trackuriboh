package sam.g.trackuriboh.managers

import sam.g.trackuriboh.R
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeZoneManager @Inject constructor() {

    private val timeZoneMap: Map<Int, String> = mapOf(
            R.string.timezone_eastern_time to "America/New_York",
            R.string.timezone_pacific_time to "America/Los_Angeles",
            R.string.timezone_mountain_time to "America/Denver",
            R.string.timezone_central_time to "America/Winnipeg",
        )

    fun getTimeZoneResourceValues() = timeZoneMap.keys.toList()

    fun getTimeZoneFromResourceValue(value: Int): TimeZone = TimeZone.getTimeZone(timeZoneMap[value])
}