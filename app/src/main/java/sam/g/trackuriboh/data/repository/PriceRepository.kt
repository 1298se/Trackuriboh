package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.ProductLocalCache
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.network.ApiResponseHandler
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.network.services.PriceApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject constructor(
    private val priceApiService: PriceApiService,
    private val productLocalCache: ProductLocalCache,
    private val apiResponseHandler: ApiResponseHandler
) {

    suspend fun getPricesForSkus(skuIds: List<Long>): Resource<List<SkuWithConditionAndPrinting>> {
        val resource = apiResponseHandler.getTCGPlayerResource {
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
        }
    }
}