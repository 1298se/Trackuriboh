package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardSet(
    @PrimaryKey(autoGenerate = false)
    val setName: String,
    val setCode: String,
    val numOfCards: Long?,
    val releaseDate: String?
)
