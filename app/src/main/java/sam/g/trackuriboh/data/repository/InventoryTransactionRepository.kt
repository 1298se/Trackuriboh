package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.InventoryTransactionLocalCache
import sam.g.trackuriboh.data.db.entities.InventoryTransaction
import sam.g.trackuriboh.data.db.entities.TransactionType
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryTransactionRepository @Inject constructor(
    private val inventoryTransactionLocalCache: InventoryTransactionLocalCache
) {
    suspend fun insertTransaction(
        type: TransactionType,
        date: Date,
        skuId: Long,
        price: Double,
        quantity: Int,
    ) = inventoryTransactionLocalCache.insertTransaction(type, date, skuId, price, quantity)

    suspend fun deleteTransaction(transaction: InventoryTransaction) =
        inventoryTransactionLocalCache.deleteTransaction(transaction)
}
