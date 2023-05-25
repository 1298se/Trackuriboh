package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.InventoryTransaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryTransactionLocalCache @Inject constructor(
    private val appDatabase: AppDatabase,
) {

    suspend fun deleteTransaction(transaction: InventoryTransaction) =
        appDatabase.inventoryTransactionDao().delete(transaction)
}
