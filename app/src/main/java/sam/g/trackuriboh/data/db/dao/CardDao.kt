package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.Card
import sam.g.trackuriboh.data.db.entities.CardWithSetInfo

@Dao
interface CardDao {
    @Query("SELECT * FROM Card WHERE id = :cardId")
    suspend fun getCardById(cardId: Long): Card?

    @Query("SELECT * FROM Card WHERE name LIKE :queryString ORDER BY name ASC")
    fun searchCardByName(queryString: String): PagingSource<Int, CardWithSetInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<Card>): List<Long>

    @Query("DELETE FROM Card")
    suspend fun deleteTable()
}
