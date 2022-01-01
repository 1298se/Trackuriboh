package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.SkuLocalCache
import sam.g.trackuriboh.data.db.entities.Sku
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkuRepository @Inject constructor(
    private val skuLocalCache: SkuLocalCache
) {

    companion object {
        // Around 250 ids seems to be the limit for a GET request with the ids sent in the URL.
        // We can send a lot more if it was changed to a POST endpoint
        const val SKU_DEFAULT_QUERY_LIMIT = 250
    }

    suspend fun getSkuIdsPaginated(offset: Int, limit: Int = SKU_DEFAULT_QUERY_LIMIT) =
        skuLocalCache.getSkuIdsPaginated(offset, limit)


    suspend fun getSkusWithConditionAndPrinting(productId: Long) =
        skuLocalCache.getSkusWithConditionAndPrinting(productId)

    suspend fun insertSkus(skus: List<Sku>) = skuLocalCache.insertSkus(skus)
}