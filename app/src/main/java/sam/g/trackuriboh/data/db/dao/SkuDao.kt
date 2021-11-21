package sam.g.trackuriboh.data.db.dao

import androidx.room.*
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting

@Dao
interface SkuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkus(skus: List<Sku>): List<Long>

    @Update(entity = Sku::class)
    suspend fun updateSkuPrices(vararg skuPrices: Sku.SkuPriceUpdate)

    @Query("SELECT * FROM Sku WHERE id IN (:skuIds)")
    suspend fun getSkusWithConditionAndPrinting(
        skuIds: List<Long>
    ): List<SkuWithConditionAndPrinting>
}
