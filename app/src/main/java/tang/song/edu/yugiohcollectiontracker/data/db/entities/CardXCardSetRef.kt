package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class CardXCardSetRef(
    @PrimaryKey(autoGenerate = false)
    val cardNumber: String,
    @ForeignKey(entity = Card::class, parentColumns = ["cardId"], childColumns = ["cardId"], onDelete = ForeignKey.CASCADE)
    val cardId: Long,
    @ForeignKey(entity = CardSet::class, parentColumns = ["setCode"], childColumns = ["setCode"], onDelete = ForeignKey.CASCADE)
    val setCode: String,
    val rarity: String?,
    val price: String?
)
