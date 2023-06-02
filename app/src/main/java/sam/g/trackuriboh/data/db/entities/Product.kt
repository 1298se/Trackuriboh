package sam.g.trackuriboh.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.types.ProductType

/**
 * !IMPORTANT
 * We use a single table to store all products, and we use [type] to distinguish between different product types.
 * We choose to do this because we (may) need to have backward reference from a [Sku] to [Product], thus we'd have to
 * search in all subtables to create the join. There also are not that many product types (or at least there isn't planned
 * support for a lot)
 */
@Parcelize
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CardSet::class,
            parentColumns = ["id"],
            childColumns = ["setId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CardRarity::class,
            parentColumns = ["id"],
            childColumns = ["rarityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("setId"), Index("rarityId")]
)
data class Product(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    // Name with no hyphens, parentheses, etc
    val cleanName: String,
    val type: ProductType,
    val imageUrl: String?,
    val setId: Long?,
    var number: String? = null,
    var rarityId: Long? = null,
    var attribute: String? = null,
    var cardType: String? = null,
    var attack: Int? = null,
    var defense: Int? = null,
    var description: String? = null,
    val marketPrice: Double? = null,
) : Parcelable {
    data class ProductPriceUpdate(
        val id: Long,
        val marketPrice: Double?
    )
}
