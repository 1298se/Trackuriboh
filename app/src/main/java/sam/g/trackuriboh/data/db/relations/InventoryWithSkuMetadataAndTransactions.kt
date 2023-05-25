package sam.g.trackuriboh.data.db.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.data.db.entities.InventoryTransaction

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
}
