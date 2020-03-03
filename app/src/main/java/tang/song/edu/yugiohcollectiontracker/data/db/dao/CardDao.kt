package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.room.*
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSets

@Dao
abstract class CardDao {
    @Query("SELECT * FROM Card WHERE cardId = :id")
    abstract suspend fun getCardById(id: Long): Card

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCard(card: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCards(card: List<Card>)

    @Transaction
    @Query("SELECT * FROM Card")
    abstract suspend fun getCardsWithSets(): List<CardWithSets>
}
