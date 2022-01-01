package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.SkuLocalCache
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.network.services.PriceApiService
import sam.g.trackuriboh.managers.NetworkRequestHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject constructor(
    private val priceApiService: PriceApiService,
    private val skuLocalCache: SkuLocalCache,
    private val networkRequestHandler: NetworkRequestHandler
) {

    /**
     * Fetches price from API and updates database
     */
    suspend fun getPricesForProductSkus(productId: Long) =
        getPricesForSkus(skuLocalCache.getSkusWithConditionAndPrinting(productId))


    suspend fun getPricesForSkuIds(skuIds: List<Long>) =
        getPricesForSkus(skuLocalCache.getSkusWithConditionAndPrinting(skuIds))

    suspend fun getPricesForSkus(
        skuWithConditionAndPrintings: List<SkuWithConditionAndPrinting>
    ) : Resource<List<SkuWithConditionAndPrinting>> {

        val skuIds = skuWithConditionAndPrintings.map { it.sku.id }

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

                // Update and fetch updated prices
                skuLocalCache.updateSkuPrices(updates)

                Resource.Success(skuLocalCache.getSkusWithConditionAndPrinting(skuIds))
            }
            is Resource.Failure -> Resource.Failure(
                resource.exception,
                skuWithConditionAndPrintings
            )
            else -> {
                Resource.Success(skuWithConditionAndPrintings)
            }
        }
    }
}