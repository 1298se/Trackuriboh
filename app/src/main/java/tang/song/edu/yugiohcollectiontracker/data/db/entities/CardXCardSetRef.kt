package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardXCardSetRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardNumber: String,
    val cardId: Long,
    val setCode: String,
    val rarity: String?,
    val price: String?
)
