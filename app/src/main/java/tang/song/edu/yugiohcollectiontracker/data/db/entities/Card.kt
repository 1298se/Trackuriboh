package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var proDeckId: Long,
    var deckId: Long,
    var name: String,
    var type: String,
    var desc: String,
    var atk: Int,
    var def: Int,
    var level: Int,
    var race: String,
    var attribute: String,
    var rarity: Int
)
