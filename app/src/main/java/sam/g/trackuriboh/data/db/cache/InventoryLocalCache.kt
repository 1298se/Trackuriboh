package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun getInventoryObservable() =
        appDatabase.inventoryDao().getInventoriesWithSkuMetadataAndTransactionsObservable()

    fun getInventoryWithSkuMetadataAndTransactionsObservable(inventoryId: Long) =
        appDatabase.inventoryDao().getInventoryWithSkuMetadataAndTransactionsObservable(inventoryId)

    suspend fun getInventory(inventoryId: Long) =
        appDatabase.inventoryDao().get(inventoryId)

    suspend fun getInventoryBySkuId(skuId: Long) =
        appDatabase.inventoryDao().getBySkuId(skuId)

    suspend fun getInventoryWithSkuMetadata(inventoryId: Long) =
        appDatabase.inventoryDao().getInventoryWithSkuMetadata(inventoryId)

    suspend fun deleteInventory(inventoryId: Long) =
        appDatabase.inventoryDao().deleteInventory(inventoryId)
}
