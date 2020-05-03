package tang.song.edu.yugiohcollectiontracker.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tang.song.edu.yugiohcollectiontracker.data.models.CardType

class Converters {
    @TypeConverter
    fun jsonToList(stringList: String): List<String> = Gson().fromJson(stringList, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun listToJson(list: List<String>): String = Gson().toJson(list)

    @TypeConverter
    fun cardTypeToString(cardType: CardType) = cardType.value

    @TypeConverter
    fun stringToCardType(cardType: String) = CardType.fromString(cardType)
}
