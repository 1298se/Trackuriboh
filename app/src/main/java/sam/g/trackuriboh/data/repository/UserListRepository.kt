package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.UserListLocalCache
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserListRepository @Inject constructor(
    private val userListLocalCache: UserListLocalCache
) {
    fun getUserListsObervable() = userListLocalCache.getUserListsObservable()

    fun getEntriesInUserListObservable(listId: Long) =
        userListLocalCache.getEntriesInUserListObservable(listId)

    suspend fun getAllUserLists() = userListLocalCache.getAllUserLists()

    suspend fun getAllUserListEntries() = userListLocalCache.getAllUserListEntries()

    suspend fun upsertUserList(userList: UserList) = userListLocalCache.upsertUserList(userList)

    suspend fun upsertUserLists(userLists: List<UserList>) =
        userListLocalCache.upsertUserLists(userLists)

    suspend fun upsertUserListEntryWithQuantity(userListEntry: UserListEntry) {
        var entry = userListLocalCache.getUserListEntry(userListEntry.listId, userListEntry.skuId)

        entry = entry?.copy(quantity = entry.quantity + userListEntry.quantity) ?: userListEntry

        userListLocalCache.upsertUserListEntry(entry)
    }

    suspend fun upsertUserListEntries(entries: List<UserListEntry>) =
        userListLocalCache.upsertUserListEntries(entries)

    suspend fun deleteUserListEntry(listId: Long, skuId: Long) {
        userListLocalCache.deleteUserListEntry(listId, skuId)
    }

    suspend fun updateUserListEntry(entry: UserListEntry) {
        if (entry.quantity > 0) {
            userListLocalCache.updateUserListEntry(entry)
        } else {
            userListLocalCache.deleteUserListEntry(entry)
        }
    }

    suspend fun updateUserList(userList: UserList) = userListLocalCache.updateUserList(userList)

    suspend fun deleteUserList(userList: UserList) = userListLocalCache.deleteUserList(userList)

    suspend fun getUserListEntry(listId: Long, skuId: Long) = userListLocalCache.getUserListEntry(listId, skuId)
}