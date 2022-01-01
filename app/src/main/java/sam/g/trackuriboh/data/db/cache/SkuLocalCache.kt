package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkuLocalCache @Inject constructor(
    private val appDatabase: AppDatabase,
) {
    suspend fun getSkuIdsPaginated(offset: Int, limit: Int) =
        appDatabase.skuDao().getSkuIdsPaginated(offset, limit)

    suspend fun getSkusWithConditionAndPrinting(skuIds: List<Long>): List<SkuWithConditionAndPrinting> =
        appDatabase.skuDao().getSkusWithConditionAndPrinting(skuIds)

    suspend fun getSkusWithConditionAndPrinting(productId: Long): List<SkuWithConditionAndPrinting> =
        appDatabase.skuDao().getSkusWithConditionAndPrinting(productId)

    suspend fun insertSkus(skus: List<Sku>) = appDatabase.skuDao().insert(skus)

    suspend fun updateSkuPrices(skuPriceUpdates: List<Sku.SkuPriceUpdate>) = appDatabase.skuDao().updateSkuPrices(skuPriceUpdates)
}
