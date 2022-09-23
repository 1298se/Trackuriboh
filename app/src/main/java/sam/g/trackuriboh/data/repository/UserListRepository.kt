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

    fun getEntriesInUserListObservable(listId: Long) = userListLocalCache.getEntriesInUserListObservable(listId)

    suspend fun getAllUserLists() = userListLocalCache.getAllUserLists()

    suspend fun getAllUserListEntries() = userListLocalCache.getAllUserListEntries()

    suspend fun upsertUserList(userList: UserList) = userListLocalCache.upsertUserList(userList)

    suspend fun upsertUserLists(userLists: List<UserList>) = userListLocalCache.upsertUserLists(userLists)

    suspend fun upsertUserListEntryAndAddQuantity(userListEntry: UserListEntry) = userListLocalCache.upsertUserListEntryAndAddQuantity(userListEntry)

    suspend fun upsertUserListEntries(entries: List<UserListEntry>) = userListLocalCache.upsertUserListEntries(entries)

    suspend fun deleteUserListEntries(listId: Long, skuIds: List<Long>?) {
        if (skuIds == null) {
            return
        }
        userListLocalCache.deleteUserListEntries(listId, skuIds)
    }

    suspend fun updateUserListEntry(entry: UserListEntry) = userListLocalCache.updateUserListEntry(entry)

    suspend fun deleteUserListEntry(entry: UserListEntry) = userListLocalCache.deleteUserListEntry(entry)

    suspend fun updateUserList(userList: UserList) = userListLocalCache.updateUserList(userList)

    suspend fun deleteUserList(userList: UserList) = userListLocalCache.deleteUserList(userList)
}