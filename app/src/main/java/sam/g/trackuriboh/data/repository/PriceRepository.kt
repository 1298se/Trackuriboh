package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.SkuLocalCache
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.network.responses.SkuPriceResponse
import sam.g.trackuriboh.data.network.services.PriceApiService
import sam.g.trackuriboh.managers.NetworkRequestHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject constructor(
    private val skuRepository: SkuRepository,
    private val priceApiService: PriceApiService,
    private val skuLocalCache: SkuLocalCache,
    private val networkRequestHandler: NetworkRequestHandler
) {
    suspend fun updatePricesForProduct(productId: Long) {
        val skuIds = skuRepository.getSkus(productId).map { it.id }
        updatePricesForSkus(skuIds)
    }

    suspend fun updatePricesForSkus(skuIds: List<Long>): Resource<SkuPriceResponse> {
        val resource = networkRequestHandler.getTCGPlayerResource {
            priceApiService.getPricesForSkus(skuIds.joinToString(","))
        }

        if (resource is Resource.Success) {
            val updates = resource.data.results.map {
                Sku.SkuPriceUpdate(
                    id = it.id,
                    lowestBasePrice = it.lowestBasePrice,
                    lowestListingPrice = it.lowestListingPrice,
                    lowestShippingPrice = it.lowestShippingPrice,
                    marketPrice = it.marketPrice
                )
            }

            // Insert update prices into database
            skuLocalCache.updateSkuPrices(updates)
        }

        return resource
    }
}
