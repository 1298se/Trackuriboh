package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(
    primaryKeys = ["listId", "skuId"],
    foreignKeys = [
        ForeignKey(
            entity = UserList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class UserListEntry(
    val listId: Long,
    // Don't want a foreign key here because then it will stop the DB download.
    val skuId: Long,
    val quantity: Int,
    val dateAdded: Date,
    @ColumnInfo(defaultValue = "0.0")
    val avgPurchasePrice: Double,
) : Parcelable
