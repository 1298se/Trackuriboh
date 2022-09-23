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

    suspend fun upsertUserList(userList: UserList) = appDatabase.userListDao().upsert(userList)

    suspend fun upsertUserLists(userLists: List<UserList>) = appDatabase.userListDao().upsert(userLists)

    suspend fun upsertUserListEntry(userListEntry: UserListEntry) = appDatabase.userListEntryDao().upsert(userListEntry)

    suspend fun upsertUserListEntries(entries: List<UserListEntry>) = appDatabase.userListEntryDao().upsert(entries)

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
