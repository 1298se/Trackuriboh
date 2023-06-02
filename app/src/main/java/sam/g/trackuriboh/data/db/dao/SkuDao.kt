package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithMetadata

@Dao
interface SkuDao : BaseDao<Sku> {
    @Transaction
    @Query("SELECT * FROM Sku WHERE productId = :productId")
    fun getSkusWithMetadataObservable(productId: Long): Flow<List<SkuWithMetadata>>

    @Query("SELECT COUNT(*) FROM Sku")
    suspend fun getSkuCount(): Int

    @Update(entity = Sku::class)
    suspend fun updateSkuPrices(skuPriceUpdates: List<Sku.SkuPriceUpdate>)

    @Query("SELECT id FROM Sku LIMIT :limit OFFSET :offset")
    suspend fun getSkuIdsPaginated(offset: Int, limit: Int): List<Long>

    @Query("SELECT * FROM Sku WHERE productId = :productId")
    suspend fun getSkus(productId: Long): List<Sku>

    @Query("DELETE FROM Sku")
    suspend fun clearTable()
}
