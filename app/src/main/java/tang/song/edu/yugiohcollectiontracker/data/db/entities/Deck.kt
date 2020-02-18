package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Deck(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String,
    var dateCreated: Long
)
