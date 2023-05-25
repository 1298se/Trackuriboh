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

    fun getEntriesInUserListObservable(listId: Long) =
        appDatabase.userListEntryDao().getUserListEntriesObservable(listId)

    suspend fun upsertUserList(userList: UserList) = appDatabase.userListDao().insert(userList)

    suspend fun upsertUserLists(userLists: List<UserList>) =
        appDatabase.userListDao().upsert(userLists)

    suspend fun upsertUserListEntry(userListEntry: UserListEntry) =
        appDatabase.userListEntryDao().upsert(userListEntry)

    suspend fun upsertUserListEntries(entries: List<UserListEntry>) =
        appDatabase.userListEntryDao().upsert(entries)

    suspend fun deleteUserListEntry(listId: Long, skuId: Long) =
        appDatabase.userListEntryDao().deleteEntry(listId, skuId)

    suspend fun updateUserListEntry(entry: UserListEntry) =
        appDatabase.userListEntryDao().update(entry)

    suspend fun deleteUserListEntry(entry: UserListEntry) =
        appDatabase.userListEntryDao().delete(entry)

    suspend fun updateUserList(userList: UserList) = appDatabase.userListDao().update(userList)

    suspend fun deleteUserList(userList: UserList) = appDatabase.userListDao().delete(userList)

    suspend fun getAllUserLists() = appDatabase.userListDao().getAllUserLists()

    suspend fun getAllUserListEntries() = appDatabase.userListEntryDao().getAllUserListEntries()

    suspend fun getUserListEntry(listId: Long, skuId: Long) = appDatabase.userListEntryDao().get(listId, skuId)
}
