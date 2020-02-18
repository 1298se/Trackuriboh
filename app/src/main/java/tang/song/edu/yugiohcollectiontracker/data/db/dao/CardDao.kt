package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.room.*
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Deck
import tang.song.edu.yugiohcollectiontracker.data.db.entities.DeckWithCards

@Dao
abstract class CardDao {
    @Transaction
    @Query("SELECT * FROM deck")
    abstract fun getAllDecksWithCards(): List<DeckWithCards>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    internal abstract fun insertDeck(deck: Deck)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract fun insertCards(cards: List<Card>)

    fun insertDeck(deck: Deck, cards: List<Card>) {

        cards.forEach { card ->
            card.deckId = deck.id
        }

        insertCards(cards)
        insertDeck(deck)
    }
}
