package sam.g.trackuriboh.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Condition(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String?,
    val abbreviation: String?,
)
