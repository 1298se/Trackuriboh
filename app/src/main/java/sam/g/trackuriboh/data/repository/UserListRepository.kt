package sam.g.trackuriboh.data.repository

import sam.g.trackuriboh.data.db.cache.CollectionLocalCache
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserListRepository @Inject constructor(
    private val collectionLocalCache: CollectionLocalCache
) {

    fun getUserListsObervable() = collectionLocalCache.getUserListsObservable()

    fun getEntriesInUserListObservable(collectionId: Long) = collectionLocalCache.getEntriesInUserListObservable(collectionId)

    suspend fun insertUserList(userList: UserList) = collectionLocalCache.insertUserList(userList)

    suspend fun insertUserListEntry(userListEntry: UserListEntry) = collectionLocalCache.insertUserListEntry(userListEntry)

    suspend fun deleteUserListEntries(listId: Long, skuIds: List<Long>?) {
        if (skuIds == null) {
            return
        }
        collectionLocalCache.deleteUserListEntries(listId, skuIds)
    }
}