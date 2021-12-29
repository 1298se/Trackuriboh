package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.UserListEntryWithSkuAndProduct

@Dao
interface UserListEntryDao : BaseDao<UserListEntry> {

    @Query("SELECT * FROM UserListEntry WHERE listId = :listId")
    fun getEntriesInUserListObservable(listId: Long): Flow<List<UserListEntryWithSkuAndProduct>>

    @Query("DELETE FROM UserListEntry WHERE listId = :listId AND skuId in (:skuIds)")
    suspend fun deleteUserListEntries(listId: Long, skuIds: List<Long>)
}
