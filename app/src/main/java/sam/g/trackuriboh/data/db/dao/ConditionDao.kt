package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import sam.g.trackuriboh.data.db.entities.Condition

@Dao
interface ConditionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConditions(conditions: List<Condition>): List<Long>
}
