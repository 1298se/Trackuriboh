package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.UserTransaction
import sam.g.trackuriboh.data.db.entities.UserListEntry

@Dao
interface UserTransactionDao: BaseDao<UserTransaction> {

    @androidx.room.Transaction
    suspend fun upsertTransactionAndUpdateUserListEntry(entry: UserListEntry, userTransaction: UserTransaction) {

    }

    @Query("DELETE FROM UserTransaction")
    suspend fun clearTable()
}