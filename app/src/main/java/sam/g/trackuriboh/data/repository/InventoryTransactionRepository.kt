package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.InventoryLocalCache
import sam.g.trackuriboh.data.db.cache.InventoryTransactionLocalCache
import sam.g.trackuriboh.data.db.entities.Inventory
import sam.g.trackuriboh.data.db.entities.InventoryTransaction
import sam.g.trackuriboh.data.db.entities.TransactionType
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryTransactionRepository @Inject constructor(
    private val inventoryLocalCache: InventoryLocalCache,
    private val inventoryTransactionLocalCache: InventoryTransactionLocalCache
) {
    suspend fun insertTransaction(
        type: TransactionType,
        date: Date,
        skuId: Long,
        price: Double,
        quantity: Int,
    ) {
        var inventory = inventoryLocalCache.getInventoryBySkuId(skuId)

        if (inventory == null) {
            inventory = Inventory(
                skuId = skuId,
                dateAdded = date,
            )
        }

        var currentQuantity = inventory.quantity
        var avgPurchasePrice = inventory.avgPurchasePrice
        var totalRealizedProfit = inventory.totalRealizedProfit

        when (type) {
            TransactionType.PURCHASE -> {
                avgPurchasePrice =
                    ((avgPurchasePrice * currentQuantity) + (price * quantity)) / (currentQuantity + quantity)
                currentQuantity += quantity
            }

            TransactionType.SALE -> {
                val saleQuantity = minOf(quantity, currentQuantity)
                currentQuantity -= saleQuantity
                totalRealizedProfit += (price - avgPurchasePrice) * saleQuantity
            }
        }

        inventoryLocalCache.upsertInventoryAndInsertTransaction(
            inventory = inventory.copy(
                quantity = currentQuantity,
                avgPurchasePrice = avgPurchasePrice,
                totalRealizedProfit = totalRealizedProfit
            ),
            transaction = InventoryTransaction(
                type = type,
                skuId = skuId,
                date = date,
                price = price,
                quantity = quantity,
                inventoryId = inventory.id
            )
        )
    }

    suspend fun deleteInventory(transaction: InventoryTransaction) =
        inventoryTransactionLocalCache.deleteTransaction(transaction)
}
