package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet

@Dao
interface CardSetDao {
    @Query("SELECT * FROM CardSet WHERE setCode = :setCode")
    suspend fun getCardSetByCode(setCode: String): CardSet

    @Query("SELECT * FROM CardSet ORDER BY setName ASC")
    fun getCardSetList(): DataSource.Factory<Int, CardSet>

    @Query("SELECT * FROM CardSet WHERE (setName LIKE :queryString)")
    fun searchCardSetByName(queryString: String): DataSource.Factory<Int, CardSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardSet(cardSet: CardSet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardSets(cardSets: List<CardSet>): List<Long>
}
