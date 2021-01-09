package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = Card::class, parentColumns = ["cardId"], childColumns = ["cardId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CardSet::class, parentColumns = ["setName"], childColumns = ["setName"], onDelete = ForeignKey.CASCADE)
    ]
)
data class CardXCardSetRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardNumber: String,
    val cardId: Long,
    var setName: String,
    val rarity: String?,
    val price: String?
)
