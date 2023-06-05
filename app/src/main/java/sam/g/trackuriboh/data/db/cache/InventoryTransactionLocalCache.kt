package sam.g.trackuriboh.data.db.cache

import androidx.room.withTransaction
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Inventory
import sam.g.trackuriboh.data.db.entities.InventoryTransaction
import sam.g.trackuriboh.data.db.entities.TransactionType
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryTransactionLocalCache @Inject constructor(
    private val appDatabase: AppDatabase,
    private val inventoryLocalCache: InventoryLocalCache,
) {

    suspend fun deleteTransaction(transaction: InventoryTransaction) =
        appDatabase.inventoryTransactionDao().delete(transaction)

    suspend fun insertTransaction(
        type: TransactionType,
        date: Date,
        skuId: Long,
        price: Double,
        quantity: Int
    ) {
        appDatabase.withTransaction {
            var inventory = inventoryLocalCache.getInventoryBySkuId(skuId)

            if (inventory == null) {
                inventory = Inventory(
                    skuId = skuId,
                    dateAdded = date,
                )
            }

            val inventoryId = appDatabase.inventoryDao().upsert(inventory)

            var transactionCandidate = InventoryTransaction(
                type = type,
                skuId = skuId,
                date = date,
                price = price,
                quantity = quantity,
                inventoryId = inventory.id
            )

            // If the inventory was a new insert, we need to set the new inventoryId
            if (inventoryId != -1L) {
                transactionCandidate = transactionCandidate.copy(inventoryId = inventoryId)
            }

            appDatabase.inventoryTransactionDao().insert(transactionCandidate)
        }
    }
}
