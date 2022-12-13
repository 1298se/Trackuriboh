package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.UserListEntryWithSkuAndProduct

@Dao
interface UserListEntryDao : BaseDao<UserListEntry> {

    @Query("SELECT * FROM UserListEntry WHERE listId = :listId ORDER BY dateAdded")
    fun getUserListEntriesObservable(listId: Long): Flow<List<UserListEntryWithSkuAndProduct>>

    @Query("SELECT * FROM UserListEntry WHERE listId = :listId AND :skuId = skuId")
    suspend fun get(listId: Long, skuId: Long): UserListEntry?

    @Query("DELETE FROM UserListEntry WHERE listId = :listId AND skuId in (:skuIds)")
    suspend fun deleteUserListEntries(listId: Long, skuIds: List<Long>)

    @Query("SELECT * FROM UserListEntry")
    suspend fun getAllUserListEntries(): List<UserListEntry>

    @Transaction
    suspend fun upsertAndAddQuantity(obj: UserListEntry): Long {
        val existingEntry = get(obj.listId, obj.skuId)

        return if (existingEntry != null) {
            update(existingEntry.copy(quantity = existingEntry.quantity + obj.quantity))

            -1
        } else {
            insert(obj)
        }
    }

    @Query("DELETE FROM UserListEntry")
    suspend fun clearTable()
}
