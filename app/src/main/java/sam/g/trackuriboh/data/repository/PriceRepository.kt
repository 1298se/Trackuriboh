package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.ProductLocalCache
import sam.g.trackuriboh.data.db.cache.SkuLocalCache
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.network.services.PriceApiService
import sam.g.trackuriboh.managers.NetworkRequestHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject constructor(
    private val priceApiService: PriceApiService,
    private val skuLocalCache: SkuLocalCache,
    private val productLocalCache: ProductLocalCache,
    private val networkRequestHandler: NetworkRequestHandler
) {
    suspend fun updatePricesForProducts(productIds: List<Long>) {
        val resource = networkRequestHandler.getTCGPlayerResource {
            priceApiService.getPricesForProducts(productIds.joinToString(","))
        }

        if (resource is Resource.Success) {
            // This API multiple items for each productId that corresponds to each "subType"
            // (something like a sku?), so we need to find the one to use
            val updates = productIds.flatMap { productId ->
                val nonNullPriceUpdateResponses =
                    resource.data.results.filter { it.id == productId && it.marketPrice != null }

                // We want to use the 1st edition, unlimited, then limited market prices in that order
                val priceUpdateResponse = (nonNullPriceUpdateResponses.find {
                    it.subTypeName == "1st Edition"
                } ?: nonNullPriceUpdateResponses.find {
                    it.subTypeName == "Unlimited"
                } ?: nonNullPriceUpdateResponses.find {
                    it.subTypeName == "Limited"
                } ?: nonNullPriceUpdateResponses.getOrNull(0))

                priceUpdateResponse?.let {
                    listOf(Product.ProductPriceUpdate(id = it.id, marketPrice = it.marketPrice))
                } ?: emptyList()
            }

            productLocalCache.updateProductPrices(updates)
        }
    }

    suspend fun updatePricesForSkus(skuIds: List<Long>) {
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
    }
}
