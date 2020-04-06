package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card

@Dao
abstract class CardDao {
    @Query("SELECT * FROM Card WHERE cardId = :cardId")
    abstract suspend fun getCardById(cardId: Long): Card

    @Query("SELECT * FROM Card ORDER BY name ASC")
    abstract fun getCardList(): DataSource.Factory<Int, Card>

    @Query("SELECT * FROM Card WHERE name LIKE :queryString")
    abstract fun searchCardByName(queryString: String): DataSource.Factory<Int, Card>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCard(card: Card): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCards(card: List<Card>): List<Long>
}
