package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import sam.g.trackuriboh.data.db.entities.Sku

@Dao
interface SkuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkus(skus: List<Sku>): List<Long>
}
