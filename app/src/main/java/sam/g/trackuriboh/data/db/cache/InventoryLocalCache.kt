package sam.g.trackuriboh.data.db.cache

import androidx.room.withTransaction
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Inventory
import sam.g.trackuriboh.data.db.entities.InventoryTransaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun getInventoryObservable() = appDatabase.inventoryDao().getInventoryObservable()

    fun getInventoryWithSkuMetadataAndTransactionsObservable(inventoryId: Long) =
        appDatabase.inventoryDao().getInventoryWithMetadataAndTransactionsObservable(inventoryId)

    suspend fun getInventory(inventoryId: Long) =
        appDatabase.inventoryDao().get(inventoryId)

    suspend fun getInventoryBySkuId(skuId: Long) =
        appDatabase.inventoryDao().getBySkuId(skuId)

    suspend fun getInventoryWithSkuMetadata(inventoryId: Long) =
        appDatabase.inventoryDao().getInventoryWithSkuMetadata(inventoryId)

    suspend fun upsertInventoryAndInsertTransaction(
        inventory: Inventory,
        transaction: InventoryTransaction
    ) {
        appDatabase.withTransaction {
            val inventoryId = appDatabase.inventoryDao().upsert(inventory)
            var transactionCandidate = transaction

            // If the inventory was a new insert, we need to set the new inventoryId
            if (inventoryId != -1L) {
                transactionCandidate = transactionCandidate.copy(inventoryId = inventoryId)
            }

            appDatabase.inventoryTransactionDao().insert(transactionCandidate)
        }
    }

    suspend fun deleteInventory(inventoryId: Long) =
        appDatabase.inventoryDao().deleteInventory(inventoryId)
}
