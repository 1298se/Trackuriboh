package tang.song.edu.yugiohcollectiontracker.data.repository

import tang.song.edu.yugiohcollectiontracker.data.db.CardInventoryLocalCache
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Transaction
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardInventoryWithTransactions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardInventoryRepository @Inject constructor(
    private val cardInventoryLocalCache: CardInventoryLocalCache
) {
    suspend fun getInventoryWithTransactions(inventoryId: Long): CardInventoryWithTransactions {
        return cardInventoryLocalCache.getInventoryWithTransactions(inventoryId)
    }

    suspend fun insertInventory(inventory: CardInventory): Long {
        return cardInventoryLocalCache.insertInventory(inventory)
    }

    suspend fun insertTransaction(transaction: Transaction): Long {
        return cardInventoryLocalCache.insertTransaction(transaction)
    }
}