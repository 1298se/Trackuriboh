package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.UserTransaction

@Dao
interface UserTransactionDao: BaseDao<UserTransaction> {
    @Query("SELECT * FROM UserTransaction WHERE listId = :listId AND skuId = :skuId ORDER BY date DESC")
    fun getUserEntryTransactionsObservable(listId: Long, skuId: Long): Flow<List<UserTransaction>>

    @Query("DELETE FROM UserTransaction")
    suspend fun clearTable()
}