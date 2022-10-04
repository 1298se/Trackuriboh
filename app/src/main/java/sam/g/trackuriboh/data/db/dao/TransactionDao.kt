package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import sam.g.trackuriboh.data.db.entities.Transaction

@Dao
interface TransactionDao: BaseDao<Transaction> {
    @Query("DELETE FROM `Transaction`")
    suspend fun clearTable()
}