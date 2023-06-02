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

    fun getSkusWithMetadataObservable(productId: Long) =
        appDatabase.skuDao().getSkusWithMetadataObservable(productId)

    suspend fun upsertSkus(skus: List<Sku>) = appDatabase.skuDao().upsert(skus)

    suspend fun updateSkuPrices(skuPriceUpdates: List<Sku.SkuPriceUpdate>) =
        appDatabase.skuDao().updateSkuPrices(skuPriceUpdates)

    suspend fun getSkus(productId: Long) =
        appDatabase.skuDao().getSkus(productId)

    suspend fun getSkuCount() = appDatabase.skuDao().getSkuCount()
}
