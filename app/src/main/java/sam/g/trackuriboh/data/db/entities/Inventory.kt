package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity
data class Inventory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val skuId: Long,
    val dateAdded: Date,
    val quantity: Int = 0,
    val avgPurchasePrice: Double = 0.0,
    val totalRealizedProfit: Double = 0.0,
) : Parcelable
