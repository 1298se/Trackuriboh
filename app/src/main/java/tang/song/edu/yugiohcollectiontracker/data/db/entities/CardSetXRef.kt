package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity

@Entity(primaryKeys = ["cardId", "setCode"])
data class CardSetXRef(
    val cardId: Long,
    val setCode: String,
    val rarity: String?
)
