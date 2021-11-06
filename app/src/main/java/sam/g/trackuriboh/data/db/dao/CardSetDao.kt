package sam.g.trackuriboh.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.CardSet

@Dao
interface CardSetDao {
    @Query("SELECT * FROM CardSet WHERE name = :setName")
    suspend fun getCardSet(setName: String): CardSet

    @Query("SELECT * FROM CardSet ORDER BY name ASC")
    fun getCardSetList(): PagingSource<Int, CardSet>

    @Query("SELECT * FROM CardSet WHERE (name LIKE :queryString)")
    fun searchCardSetByName(queryString: String): PagingSource<Int, CardSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardSet(cardSet: CardSet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardSets(cardSets: List<CardSet>): List<Long>

    @Query("DELETE FROM CardSet")
    suspend fun deleteTable()

    @Query("SELECT * FROM CardSet WHERE (name LIKE :queryString)")
    suspend fun _searchCardSet(queryString: String): List<CardSet>
}
