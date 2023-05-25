package sam.g.trackuriboh.data.db.entities

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.R
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Inventory::class,
            parentColumns = ["id"],
            childColumns = ["inventoryId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index("inventoryId")]
)
@Parcelize
data class InventoryTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TransactionType,
    val date: Date,
    val skuId: Long,
    val price: Double,
    val quantity: Int,
    val inventoryId: Long,
) : Parcelable

enum class TransactionType {
    PURCHASE,
    SALE;

    fun getDisplayStringRes(context: Context) =
        when (this) {
            PURCHASE -> context.getString(R.string.lbl_purchase)
            SALE -> context.getString(R.string.lbl_sale)
        }
}
