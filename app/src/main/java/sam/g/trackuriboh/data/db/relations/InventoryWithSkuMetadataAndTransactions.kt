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

    /**
     * The quantity currently in inventory.
     */
    @IgnoredOnParcel
    @Ignore
    val quantity: Int

    @IgnoredOnParcel
    @Ignore
    val totalPurchaseAmount: Double

    @IgnoredOnParcel
    @Ignore
    val averagePurchasePrice: Double

    @IgnoredOnParcel
    @Ignore
    val totalValue: Double

    @IgnoredOnParcel
    @Ignore
    val unrealizedProfitPerCard: Double?

    @IgnoredOnParcel
    @Ignore
    val totalUnrealizedProfit: Double?

    @IgnoredOnParcel
    @Ignore
    val unrealizedProfitPercentPerCard: Int?

    @IgnoredOnParcel
    @Ignore
    val totalRealizedProfit: Double

    init {
        val purchaseTransactions = transactions.filter { it.type == TransactionType.PURCHASE }
        val totalPurchaseQuantity = purchaseTransactions.sumOf { it.quantity }

        totalPurchaseAmount = purchaseTransactions.sumOf { it.price * it.quantity }
        averagePurchasePrice = totalPurchaseAmount / totalPurchaseQuantity

        val saleTransactions = transactions.filter { it.type == TransactionType.SALE }

        val totalSaleQuantity = saleTransactions.sumOf { it.quantity }
        val totalSaleAmount = saleTransactions.sumOf { it.price * it.quantity }

        totalRealizedProfit = totalSaleAmount - (averagePurchasePrice * totalSaleQuantity)

        unrealizedProfitPerCard =
            inventoryWithSkuMetadata.skuWithMetadata.sku.lowestListingPrice?.minus(
                averagePurchasePrice
            )

        quantity = transactions.fold(0) { acc, inventoryTransaction ->
            if (inventoryTransaction.type == TransactionType.PURCHASE) {
                acc + inventoryTransaction.quantity
            } else {
                acc - inventoryTransaction.quantity
            }
        }

        totalValue =
            inventoryWithSkuMetadata.skuWithMetadata.sku.lowestListingPrice?.times(quantity) ?: 0.0
        totalUnrealizedProfit = unrealizedProfitPerCard?.times(quantity)

        unrealizedProfitPercentPerCard =
            unrealizedProfitPerCard?.div(averagePurchasePrice)?.times(100)?.toInt()
    }
}
