package sam.g.trackuriboh.data.db.cache

import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserListLocalCache @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun getUserListsObservable() = appDatabase.userListDao().getUserListsObservable()

    fun getEntriesInUserListObservable(listId: Long) = appDatabase.userListEntryDao().getEntriesInUserListObservable(listId)

    suspend fun insertUserList(userList: UserList) = appDatabase.userListDao().insert(userList)

    suspend fun insertUserLists(userLists: List<UserList>) = appDatabase.userListDao().insert(userLists)

    suspend fun insertUserListEntry(userListEntry: UserListEntry) = appDatabase.userListEntryDao().insert(userListEntry)

    suspend fun insertUserListEntries(entries: List<UserListEntry>) = appDatabase.userListEntryDao().insert(entries)

    suspend fun deleteUserListEntries(listId: Long, skuIds: List<Long>) =
        appDatabase.userListEntryDao().deleteUserListEntries(listId, skuIds)

    suspend fun updateUserListEntry(entry: UserListEntry) =
        appDatabase.userListEntryDao().update(entry)

    suspend fun deleteUserListEntry(entry: UserListEntry) =
        appDatabase.userListEntryDao().delete(entry)

    suspend fun updateUserList(userList: UserList) = appDatabase.userListDao().update(userList)

    suspend fun deleteUserList(userList: UserList) = appDatabase.userListDao().delete(userList)

    suspend fun getAllUserLists() = appDatabase.userListDao().getAllUserLists()

    suspend fun getAllUserListEntries() = appDatabase.userListEntryDao().getAllUserListEntries()

}
