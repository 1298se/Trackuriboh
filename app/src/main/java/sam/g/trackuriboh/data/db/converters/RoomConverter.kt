package sam.g.trackuriboh.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    @TypeConverter
    fun jsonToList(stringList: String?): List<String>? = Gson().fromJson(stringList, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun listToJson(list: List<String>?): String? = Gson().toJson(list)
}
