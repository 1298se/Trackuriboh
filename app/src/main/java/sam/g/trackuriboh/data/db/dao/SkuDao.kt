package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting

@Dao
interface SkuDao : BaseDao<Sku> {
    @Update(entity = Sku::class)
    suspend fun updateSkuPrices(skuPriceUpdates: List<Sku.SkuPriceUpdate>)

    @Query("SELECT * FROM Sku WHERE id IN (:skuIds)")
    suspend fun getSkusWithConditionAndPrinting(
        skuIds: List<Long>
    ): List<SkuWithConditionAndPrinting>

    @Query("DELETE FROM Sku")
    suspend fun clearTable()
}
