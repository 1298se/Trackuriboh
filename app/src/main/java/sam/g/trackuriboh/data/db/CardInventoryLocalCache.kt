package sam.g.trackuriboh.data.db

import androidx.paging.PagingSource
import sam.g.trackuriboh.data.db.entities.CardInventory
import sam.g.trackuriboh.data.db.entities.Transaction
import sam.g.trackuriboh.data.db.relations.CardInventoryWithTransactions
import sam.g.trackuriboh.data.types.EditionType
import javax.inject.Inject

class CardInventoryLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun getInventoryList(): PagingSource<Int, CardInventory> {
        return appDatabase.cardInventoryDao().getInventoryList()
    }

    suspend fun getInventory(cardNumber: String, rarity: String?, edition: EditionType?): CardInventory? {
        return appDatabase.cardInventoryDao().getInventory(cardNumber, rarity, edition)
    }

    suspend fun getInventoryWithTransactions(inventoryId: Long): CardInventoryWithTransactions {
        return appDatabase.cardInventoryXTransactionDao().getCardInventoryWithTransaction(inventoryId)
    }

    suspend fun insertInventory(inventory: CardInventory): Long {
        return appDatabase.cardInventoryDao().insertInventory(inventory)
    }

    suspend fun insertTransaction(transaction: Transaction): Long {
        return appDatabase.transactionDao().insertTransaction(transaction)
    }
}
