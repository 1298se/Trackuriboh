package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet

@Dao
interface CardSetDao {
    @Query("SELECT * FROM CardSet WHERE setCode = :setCode")
    suspend fun getSetByCode(setCode: String): CardSet

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(cardSet: CardSet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(set: List<CardSet>): List<Long>
}
