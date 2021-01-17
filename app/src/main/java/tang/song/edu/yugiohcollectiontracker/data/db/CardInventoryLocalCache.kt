package tang.song.edu.yugiohcollectiontracker.data.db

import androidx.paging.PagingSource
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Transaction
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardInventoryWithTransactions
import tang.song.edu.yugiohcollectiontracker.data.types.EditionType
import javax.inject.Inject

class CardInventoryLocalCache @Inject constructor(
    private val cardDatabase: CardDatabase
) {
    fun getInventoryList(): PagingSource<Int, CardInventory> {
        return cardDatabase.cardInventoryDao().getInventoryList()
    }

    suspend fun getInventory(cardNumber: String, rarity: String?, edition: EditionType?): CardInventory? {
        return cardDatabase.cardInventoryDao().getInventory(cardNumber, rarity, edition)
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
