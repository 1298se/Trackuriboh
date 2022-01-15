package sam.g.trackuriboh.data.db.relations

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.UserList

@Parcelize
data class UserListWithCountAndTotal(
    @Embedded val userList: UserList,
    val totalCount: Int,
    val totalValue: Double,
    val productImageUrl: String?
) : Parcelable
