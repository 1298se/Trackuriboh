package sam.g.trackuriboh.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import sam.g.trackuriboh.data.db.ProductLocalCache
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.network.services.PriceApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject constructor(
    private val priceApiService: PriceApiService,
    private val productLocalCache: ProductLocalCache
) {

    suspend fun getPricesForSkus(skuIds: List<Long>): Flow<List<SkuWithConditionAndPrinting>> {
        val response = priceApiService.getPricesForSkus(skuIds.joinToString(","))

        if (response.isSuccessful) {
            val updates = response.body()?.results?.map { Sku.SkuPriceUpdate(
                it.id, it.lowestListingPrice, it.lowestShippingPrice, it.marketPrice
            ) } ?: emptyList()

            productLocalCache.updateSkuPrices(updates)
        }

        return productLocalCache.getSkusWithConditionAndPrinting(skuIds).flowOn(Dispatchers.IO)
    }
}