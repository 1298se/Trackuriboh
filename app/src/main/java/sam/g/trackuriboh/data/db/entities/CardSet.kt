package sam.g.trackuriboh.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardSet(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String?,
    val code: String?,
    val releaseDate: String?
)
