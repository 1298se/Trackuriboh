package sam.g.trackuriboh.data.db.converters

import androidx.room.TypeConverter
import java.util.*

class RoomConverter {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
