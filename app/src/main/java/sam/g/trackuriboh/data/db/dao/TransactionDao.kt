package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.Transaction
import sam.g.trackuriboh.data.db.entities.UserListEntry

@Dao
interface TransactionDao: BaseDao<Transaction> {

    @androidx.room.Transaction
    suspend fun upsertTransactionAndUpdateUserListEntry(entry: UserListEntry, transaction: Transaction) {

    }

    @Query("DELETE FROM `Transaction`")
    suspend fun clearTable()
}