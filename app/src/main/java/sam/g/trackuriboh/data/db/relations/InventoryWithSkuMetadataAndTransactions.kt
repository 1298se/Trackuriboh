package sam.g.trackuriboh.data.db.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.InventoryTransaction
import sam.g.trackuriboh.data.db.entities.TransactionType

@Parcelize
data class InventoryWithSkuMetadataAndTransactions(
    @Embedded val inventoryWithSkuMetadata: InventoryWithSkuMetadata,

    @Relation(
        entity = InventoryTransaction::class,
        parentColumn = "id",
        entityColumn = "inventoryId",
    )
    val transactions: List<InventoryTransaction>
) : Parcelable {
    fun getSortedTransactionsByDateDesc() =
        transactions.sortedByDescending { it.date }

    @IgnoredOnParcel
    @Ignore
    val quantity = transactions.fold(0) { acc, inventoryTransaction ->
        if (inventoryTransaction.type == TransactionType.PURCHASE) {
            acc + inventoryTransaction.quantity
        } else {
            acc - inventoryTransaction.quantity
        }
    }

    @IgnoredOnParcel
    @Ignore
    val totalPurchaseAmount =
        transactions.filter { it.type == TransactionType.PURCHASE }.sumOf { it.price * it.quantity }

    @IgnoredOnParcel
    @Ignore
    val averagePurchasePrice = if (quantity != 0) totalPurchaseAmount / quantity else 0.0

    @IgnoredOnParcel
    @Ignore
    val totalValue =
        inventoryWithSkuMetadata.skuWithMetadata.sku.lowestListingPrice?.times(quantity)

    @IgnoredOnParcel
    @Ignore
    val unrealizedProfitPerCard =
        inventoryWithSkuMetadata.skuWithMetadata.sku.lowestListingPrice?.minus(averagePurchasePrice)

    @IgnoredOnParcel
    @Ignore
    val totalUnrealizedProfit = unrealizedProfitPerCard?.times(quantity)

    @IgnoredOnParcel
    @Ignore
    val unrealizedProfitPercentPerCard =
        unrealizedProfitPerCard?.div(averagePurchasePrice)?.times(100)?.toInt()

    @IgnoredOnParcel
    @Ignore
    val totalRealizedProfit: Double


    init {
        val saleTransactions = transactions.filter { it.type == TransactionType.SALE }

        val totalSaleQuantity = saleTransactions.sumOf { it.quantity }
        val totalSaleAmount = saleTransactions.sumOf { it.price * it.quantity }

        totalRealizedProfit = totalSaleAmount - (averagePurchasePrice * totalSaleQuantity)
    }
}
