package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.SkuLocalCache
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.network.responses.SkuPriceResponse
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

    suspend fun getPricesForProductSkusOrdered(productId: Long) =
        getPricesForSkus(skuLocalCache.getSkusWithConditionAndPrintingOrdered(productId))


    suspend fun getPricesForSkuIds(skuIds: List<Long>) =
        getPricesForSkus(skuLocalCache.getSkusWithConditionAndPrinting(skuIds))

    suspend fun updatePricesForSkus(skuIds: List<Long>): Resource<SkuPriceResponse> {
        val resource = networkRequestHandler.getTCGPlayerResource {
            priceApiService.getPricesForSkus(skuIds.joinToString(","))
        }

        if (resource is Resource.Success) {
            val updates = resource.data.results.map {
                Sku.SkuPriceUpdate(
                    it.id, it.lowestListingPrice, it.lowestShippingPrice, it.marketPrice
                )
            }

            // Insert update prices into database
            skuLocalCache.updateSkuPrices(updates)
        }

        return resource
    }


    private suspend fun getPricesForSkus(
        skuWithConditionAndPrintings: List<SkuWithConditionAndPrinting>
    ) : Resource<List<SkuWithConditionAndPrinting>> {

        // If the list is empty, we should not call the API because it will result in 404.
        if (skuWithConditionAndPrintings.isEmpty()) {
            return Resource.Success(emptyList())
        }

        val skuIds = skuWithConditionAndPrintings.map { it.sku.id }

        // Fetch updated prices and insert them into DB
        return when (val resource = updatePricesForSkus(skuIds)) {
            is Resource.Success -> {
                // If the update was successful, return updated values
                Resource.Success(skuLocalCache.getSkusWithConditionAndPrintingOrdered(skuIds))
            }
            // Otherwise, return what we already have,
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
