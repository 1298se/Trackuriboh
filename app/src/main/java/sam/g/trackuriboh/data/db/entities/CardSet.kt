package sam.g.trackuriboh.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardSet(
    @PrimaryKey(autoGenerate = false)
    val groupId: Long,
    val name: String?,
    val abbreviation: String?,
    val publishedOn: String?
)
