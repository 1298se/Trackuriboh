package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.network.NetworkRequestHandler
import sam.g.trackuriboh.data.network.services.CatalogApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepository @Inject constructor(
    private val catalogApiService: CatalogApiService,
    private val networkRequestHandler: NetworkRequestHandler
) {
    suspend fun fetchCardRarities() = networkRequestHandler.getTCGPlayerResource { catalogApiService.getCardRarities() }

    suspend fun fetchProductPrintings() = networkRequestHandler.getTCGPlayerResource { catalogApiService.getPrintings() }

    suspend fun fetchProductConditions() = networkRequestHandler.getTCGPlayerResource{ catalogApiService.getConditions() }
}
