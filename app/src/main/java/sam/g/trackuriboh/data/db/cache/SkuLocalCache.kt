package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Sku
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkuLocalCache @Inject constructor(
    private val appDatabase: AppDatabase,
) {
    suspend fun getSkuIdsPaginated(offset: Int, limit: Int) =
        appDatabase.skuDao().getSkuIdsPaginated(offset, limit)

    suspend fun getSkusWithConditionAndPrintingOrdered(skuIds: List<Long>) =
        appDatabase.skuDao().getSkusWithConditionAndPrintingOrdered(skuIds)

    suspend fun getSkusWithConditionAndPrintingOrdered(productId: Long) =
        appDatabase.skuDao().getSkusWithConditionAndPrintingOrdered(productId)

    suspend fun getSkusWithConditionAndPrinting(skuIds: List<Long>) =
        appDatabase.skuDao()._getSkusWithConditionAndPrinting(skuIds)

    suspend fun insertSkus(skus: List<Sku>) = appDatabase.skuDao().insert(skus)

    suspend fun updateSkuPrices(skuPriceUpdates: List<Sku.SkuPriceUpdate>) = appDatabase.skuDao().updateSkuPrices(skuPriceUpdates)
}
