package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.Condition

@Dao
interface ConditionDao : BaseDao<Condition> {
    @Query("DELETE FROM Condition")
    suspend fun clearTable()
}
