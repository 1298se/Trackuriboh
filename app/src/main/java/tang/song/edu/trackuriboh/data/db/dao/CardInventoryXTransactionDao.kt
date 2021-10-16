package tang.song.edu.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import tang.song.edu.trackuriboh.data.db.relations.CardInventoryWithTransactions

@Dao
abstract class CardInventoryXTransactionDao : CardInventoryDao, TransactionDao {
    @Transaction
    @Query("SELECT * FROM CardInventory WHERE inventoryId = :inventoryId")
    abstract suspend fun getCardInventoryWithTransaction(inventoryId: Long): CardInventoryWithTransactions
}
