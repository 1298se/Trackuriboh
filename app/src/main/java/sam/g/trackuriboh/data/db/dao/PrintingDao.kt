package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.Printing

@Dao
interface PrintingDao : BaseDao<Printing> {
    @Query("DELETE FROM Printing")
    suspend fun clearTable()
}