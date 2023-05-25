package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.InventoryLocalCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepository @Inject constructor(
    private val inventoryLocalCache: InventoryLocalCache
) {
    fun getInventoryObservable() = inventoryLocalCache.getInventoryObservable()

    fun getInventoryWithSkuMetadataAndTransactionsObservable(inventoryId: Long) =
        inventoryLocalCache.getInventoryWithSkuMetadataAndTransactionsObservable(inventoryId)

    suspend fun getInventoryWithSkuMetadata(inventoryId: Long) =
        inventoryLocalCache.getInventoryWithSkuMetadata(inventoryId)

    suspend fun deleteInventory(inventoryId: Long) =
        inventoryLocalCache.deleteInventory(inventoryId)
}
