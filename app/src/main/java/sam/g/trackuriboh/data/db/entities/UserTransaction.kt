package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserListEntry::class,
            parentColumns = ["listId", "skuId"],
            childColumns = ["listId", "skuId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index(value = ["productId"]), Index(value = ["listId"]), Index(value = ["skuId"])]
)
@Parcelize
data class UserTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TransactionType,
    val productId: Long,
    val listId: Long,
    val skuId: Long,
    val quantity: Int,
    val price: Double,
    val date: Date,
) : Parcelable

enum class TransactionType {
    PURCHASE,
    SALE
}
