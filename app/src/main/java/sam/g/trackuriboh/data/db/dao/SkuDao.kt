package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting

@Dao
interface SkuDao : BaseDao<Sku> {
    @Update(entity = Sku::class)
    suspend fun updateSkuPrices(skuPriceUpdates: List<Sku.SkuPriceUpdate>)

    @Transaction
    @Query("SELECT * FROM Sku WHERE id IN (:skuIds)")
    suspend fun _getSkusWithConditionAndPrinting(skuIds: List<Long>): List<SkuWithConditionAndPrinting>

    @Transaction
    @Query("SELECT * FROM Sku WHERE productId = :productId")
    suspend fun _getSkusWithConditionAndPrinting(productId: Long): List<SkuWithConditionAndPrinting>

    suspend fun getSkusWithConditionAndPrintingOrdered(skuIds: List<Long>): List<SkuWithConditionAndPrinting> {
        val skusWithConditionAndPrinting = _getSkusWithConditionAndPrinting(skuIds)

        return orderByPrintingAndCondition(skusWithConditionAndPrinting)
    }

    suspend fun getSkusWithConditionAndPrintingOrdered(productId: Long): List<SkuWithConditionAndPrinting> {
        val skusWithConditionAndPrinting = _getSkusWithConditionAndPrinting(productId)

        return orderByPrintingAndCondition(skusWithConditionAndPrinting)
    }

    @Query("SELECT id FROM Sku ORDER BY id LIMIT :limit OFFSET :offset")
    suspend fun getSkuIdsPaginated(offset: Int, limit: Int): List<Long>

    @Query("DELETE FROM Sku")
    suspend fun clearTable()

    private fun orderByPrintingAndCondition(skusWithConditionAndPrinting: List<SkuWithConditionAndPrinting>) =
        skusWithConditionAndPrinting.sortedWith(compareBy(
            { it.printing?.order ?: Integer.MAX_VALUE },
            { it.condition?.order ?: Integer.MAX_VALUE }
        ))
}
