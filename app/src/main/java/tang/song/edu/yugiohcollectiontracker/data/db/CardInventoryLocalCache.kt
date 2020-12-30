package tang.song.edu.yugiohcollectiontracker.data.db

import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Transaction
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardInventoryWithTransactions
import javax.inject.Inject

class CardInventoryLocalCache @Inject constructor(
    private val cardDatabase: CardDatabase
) {

    suspend fun getInventory(cardNumber: String, rarity: String): CardInventory? {
        return cardDatabase.cardInventoryDao().getInventory(cardNumber, rarity)
    }

    suspend fun getInventoryWithTransactions(inventoryId: Long): CardInventoryWithTransactions {
        return cardDatabase.cardInventoryXTransactionDao().getCardInventoryWithTransaction(inventoryId)
    }

    suspend fun insertInventory(inventory: CardInventory): Long {
        return cardDatabase.cardInventoryDao().insertInventory(inventory)
    }

    suspend fun insertTransaction(transaction: Transaction): Long {
        return cardDatabase.transactionDao().insertTransaction(transaction)
    }
}
