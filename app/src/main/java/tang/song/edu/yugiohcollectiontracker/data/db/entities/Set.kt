package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Set(
    @PrimaryKey(autoGenerate = false)
    val setCode: String,
    val setName: String,
    val size: Long,
    val releaseDate: String
)
