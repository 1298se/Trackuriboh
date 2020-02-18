package tang.song.edu.yugiohcollectiontracker.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class DeckWithCards(
    @Embedded
    val deck: Deck,
    @Relation(parentColumn = "id", entityColumn = "deckId")
    val deckList: List<Card>
)