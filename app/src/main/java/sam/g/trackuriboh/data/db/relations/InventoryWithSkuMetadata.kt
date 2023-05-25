package sam.g.trackuriboh.data.db.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.Inventory
import sam.g.trackuriboh.data.db.entities.Sku

@Parcelize
data class InventoryWithSkuMetadata(
    @Embedded val inventory: Inventory,

    @Relation(
        entity = Sku::class,
        parentColumn = "skuId",
        entityColumn = "id",
    )
    val skuWithMetadata: SkuWithMetadata,
) : Parcelable {
    fun getTotalValue() = skuWithMetadata.sku.lowestBasePrice?.times(inventory.quantity)

    fun getUnrealizedProfitPerCard() =
        skuWithMetadata.sku.lowestBasePrice?.minus(inventory.avgPurchasePrice)

    fun getTotalUnrealizedProfit() = getUnrealizedProfitPerCard()?.times(inventory.quantity)

    fun getUnrealizedProfitPercentagePerCard() =
        getUnrealizedProfitPerCard()?.div(inventory.avgPurchasePrice)?.times(100)?.toInt()

    fun getTotalCost() = inventory.avgPurchasePrice * inventory.quantity
}
