package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.UserList

@Dao
interface UserListDao : BaseDao<UserList> {

    @Query("SELECT * FROM UserList ORDER BY creationDate")
    fun getUserListsObservable(): Flow<List<UserList>>

    @Query("SELECT * FROM UserList")
    suspend fun getAllUserLists(): List<UserList>

    @Query("DELETE FROM UserList")
    suspend fun clearTable()
}
