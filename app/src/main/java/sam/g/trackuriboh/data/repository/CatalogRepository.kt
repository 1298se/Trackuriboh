package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.CatalogLocalCache
import sam.g.trackuriboh.data.db.entities.CardRarity
import sam.g.trackuriboh.data.db.entities.Condition
import sam.g.trackuriboh.data.db.entities.Printing
import sam.g.trackuriboh.data.network.services.CatalogApiService
import sam.g.trackuriboh.managers.NetworkRequestHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepository @Inject constructor(
    private val catalogApiService: CatalogApiService,
    private val catalogLocalCache: CatalogLocalCache,
    private val networkRequestHandler: NetworkRequestHandler
) {
    suspend fun fetchCardRarities() = networkRequestHandler.getTCGPlayerResource { catalogApiService.getCardRarities() }

    suspend fun fetchProductPrintings() = networkRequestHandler.getTCGPlayerResource { catalogApiService.getPrintings() }

    suspend fun fetchProductConditions() = networkRequestHandler.getTCGPlayerResource{ catalogApiService.getConditions() }

    suspend fun upsertCardRarities(rarities: List<CardRarity>) =
        catalogLocalCache.upsertCardRarities(rarities)

    suspend fun upsertConditions(conditions: List<Condition>) =
        catalogLocalCache.upsertConditions(conditions)

    suspend fun upsertPrintings(printings: List<Printing>) =
        catalogLocalCache.upsertPrintings(printings)

    suspend fun getCardRarityByName(name: String) =
        catalogLocalCache.getCardRarityByName(name)
}
