package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Condition::class,
            parentColumns = ["id"],
            childColumns = ["conditionId"],
        ),
        ForeignKey(
            entity = Printing::class,
            parentColumns = ["id"],
            childColumns = ["printingId"],
        )
    ],
    indices = [Index("productId"), Index("conditionId"), Index("printingId")]
)
data class Sku(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val productId: Long,
    val printingId: Long?,
    val conditionId: Long?,
    val lowestListingPrice: Double? = null,
    val lowestBasePrice: Double? = null,
    val lowestShippingPrice: Double? = null,
    val marketPrice: Double? = null,
) : Parcelable {

    data class SkuPriceUpdate(
        val id: Long,
        val lowestListingPrice: Double?,
        val lowestBasePrice: Double?,
        val lowestShippingPrice: Double?,
        val marketPrice: Double?
    )
}
