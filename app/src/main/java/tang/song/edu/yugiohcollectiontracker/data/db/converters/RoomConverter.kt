package tang.song.edu.yugiohcollectiontracker.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tang.song.edu.yugiohcollectiontracker.data.types.*
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

    @TypeConverter
    fun cardTypeToString(cardType: CardType?) = cardType?.value

    @TypeConverter
    fun stringToCardType(cardType: String?) = CardType.fromString(cardType)

    @TypeConverter
    fun transactionTypeToInt(transactionType: TransactionType?) = transactionType?.value

    @TypeConverter
    fun intToTransactionType(transactionType: Int?) = TransactionType.fromInt(transactionType)

    @TypeConverter
    fun platformTypeToInt(platformType: PlatformType?) = platformType?.value

    @TypeConverter
    fun intToPlatformType(platformType: Int?) = PlatformType.fromInt(platformType)

    @TypeConverter
    fun editionTypeToInt(editionType: EditionType?) = editionType?.value

    @TypeConverter
    fun intToEditionType(editionType: Int?) = EditionType.fromInt(editionType)

    @TypeConverter
    fun conditionTypeToInt(conditionType: ConditionType?) = conditionType?.value

    @TypeConverter
    fun intToConditionType(conditionType: Int?) = ConditionType.fromInt(conditionType)
}
