package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.UserListEntryWithSkuMetadata

@Dao
interface UserListEntryDao : BaseDao<UserListEntry> {

    @Transaction
    @Query("SELECT * FROM UserListEntry WHERE listId = :listId ORDER BY dateAdded")
    fun getUserListEntriesObservable(listId: Long): Flow<List<UserListEntryWithSkuMetadata>>

    @Query("SELECT * FROM UserListEntry WHERE listId = :listId AND :skuId = skuId")
    suspend fun get(listId: Long, skuId: Long): UserListEntry?

    @Query("DELETE FROM UserListEntry WHERE listId = :listId AND skuId = :skuId")
    suspend fun deleteEntry(listId: Long, skuId: Long)

    @Query("SELECT * FROM UserListEntry")
    suspend fun getAllUserListEntries(): List<UserListEntry>

    @Query("DELETE FROM UserListEntry")
    suspend fun clearTable()
}
