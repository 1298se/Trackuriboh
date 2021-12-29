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

    suspend fun insertUserList(userList: UserList) = userListLocalCache.insertUserList(userList)

    suspend fun insertUserListEntry(userListEntry: UserListEntry) = userListLocalCache.insertUserListEntry(userListEntry)

    suspend fun deleteUserListEntries(listId: Long, skuIds: List<Long>?) {
        if (skuIds == null) {
            return
        }
        userListLocalCache.deleteUserListEntries(listId, skuIds)
    }
}