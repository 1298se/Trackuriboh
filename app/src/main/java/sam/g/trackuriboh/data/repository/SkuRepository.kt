package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.SkuLocalCache
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.workers.GET_REQUEST_ID_QUERY_LIMIT
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkuRepository @Inject constructor(
    private val skuLocalCache: SkuLocalCache
) {
    suspend fun getSkus(productId: Long) =
        skuLocalCache.getSkus(productId)

    fun getSkusWithMetadataObservable(productId: Long) =
        skuLocalCache.getSkusWithMetadataObservable(productId)

    suspend fun getSkuIdsPaginated(offset: Int, limit: Int = GET_REQUEST_ID_QUERY_LIMIT) =
        skuLocalCache.getSkuIdsPaginated(offset, limit)

    suspend fun upsertSkus(skus: List<Sku>) = skuLocalCache.upsertSkus(skus)
}