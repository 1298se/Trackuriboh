package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import sam.g.trackuriboh.data.db.entities.Printing

@Dao
interface PrintingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrintings(printings: List<Printing>): List<Long>
}
