package tang.song.edu.yugiohcollectiontracker.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun jsonToList(stringList: String) = Gson().fromJson<List<String>>(stringList, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun listToJson(list: List<String>) = Gson().toJson(list)
}
