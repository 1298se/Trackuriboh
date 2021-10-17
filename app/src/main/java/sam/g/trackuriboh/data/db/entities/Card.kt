package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.network.responses.CardResponse

@Parcelize
@Entity
data class Card(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val cleanName: String?,
    val imageUrl: String?,
    val setId: Long?,
    var number: String? = null,
    var rarity: String? = null,
    var attribute: String? = null,
    var cardType: String? = null,
    var attack: Int? = null,
    var defense: Int? = null,
    var description: String? = null,
) : Parcelable {

    constructor(
        id: Long,
        name: String,
        cleanName: String?,
        imageUrl: String?,
        setId: Long?,
        extendedData: List<CardResponse.ExtendedDataItem>
    ) : this(id, name, cleanName, imageUrl, setId) {
        parseExtendedData(extendedData)
    }

    private enum class ExtendedDataFields(val field: String) {
        NUMBER("Number"),
        RARITY("Rarity"),
        ATTRIBUTE("Attribute"),
        CARD_TYPE("Card Type"),
        ATTACK("Attack"),
        DEFENSE("Defense"),
        DESCRIPTION("Description")
    }

    private fun parseExtendedData(extendedData: List<CardResponse.ExtendedDataItem>) {
        val extendedDataMap = extendedData.associate { it.name to it.value }

        this.number = extendedDataMap[ExtendedDataFields.NUMBER.field]
        this.rarity = extendedDataMap[ExtendedDataFields.RARITY.field]
        this.attribute = extendedDataMap[ExtendedDataFields.ATTRIBUTE.field]
        this.cardType = extendedDataMap[ExtendedDataFields.CARD_TYPE.field]
        this.attack = extendedDataMap[ExtendedDataFields.ATTACK.field]?.toInt()
        this.defense = extendedDataMap[ExtendedDataFields.DEFENSE.field]?.toInt()
        this.description = extendedDataMap[ExtendedDataFields.DESCRIPTION.field]
    }
}
