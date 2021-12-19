package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.ProductLocalCache
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.network.NetworkRequestHandler
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.network.services.PriceApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject constructor(
    private val priceApiService: PriceApiService,
    private val productLocalCache: ProductLocalCache,
    private val networkRequestHandler: NetworkRequestHandler
) {

    suspend fun getPricesForSkus(skuIds: List<Long>): Resource<List<SkuWithConditionAndPrinting>> {
        val resource = networkRequestHandler.getTCGPlayerResource {
            priceApiService.getPricesForSkus(skuIds.joinToString(","))
        }

        return when (resource) {
            is Resource.Success -> {
                val updates = resource.data.results.map {
                    Sku.SkuPriceUpdate(
                        it.id, it.lowestListingPrice, it.lowestShippingPrice, it.marketPrice
                    )
                }

                productLocalCache.updateSkuPrices(updates)

                Resource.Success(productLocalCache.getSkusWithConditionAndPrinting(skuIds))
            }
            is Resource.Failure -> Resource.Failure(
                resource.exception,
                productLocalCache.getSkusWithConditionAndPrinting(skuIds)
            )
            else -> {
                Resource.Success(productLocalCache.getSkusWithConditionAndPrinting(skuIds))
            }
        }
    }
}