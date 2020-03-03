package tang.song.edu.yugiohcollectiontracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Set

@Dao
abstract class SetDao {
    @Query("SELECT * FROM `Set` WHERE setCode = :setCode")
    abstract suspend fun getSetByCode(setCode: String): Set

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSet(set: Set)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSets(set: List<Set>)
}
