package sam.g.trackuriboh.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.relations.UserListWithCountAndTotal

@Dao
interface UserListDao : BaseDao<UserList> {

    @Query("SELECT UserList.*, " +
            "TOTAL(UserListEntry.quantity) AS totalCount, " +
            "TOTAL(UserListEntry.quantity * Sku.lowestBasePrice) AS totalValue, " +
            "Product.imageUrl AS productImageUrl " +
            "FROM UserList " +
            "LEFT JOIN UserListEntry ON UserList.id = UserListEntry.listId " +
            "LEFT JOIN Sku ON UserListEntry.skuId = Sku.id " +
            "LEFT JOIN Product ON Product.id = Sku.productId " +
            "GROUP BY UserList.id " +
            "HAVING MIN(UserListEntry.dateAdded)"
    )
    fun getUserListsObservable(): Flow<List<UserListWithCountAndTotal>>

    @Query("SELECT * FROM UserList")
    suspend fun getAllUserLists(): List<UserList>

    @Query("DELETE FROM UserList")
    suspend fun clearTable()
}
