package sam.g.trackuriboh.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import sam.g.trackuriboh.data.db.entities.CardInventory
import sam.g.trackuriboh.data.db.entities.Transaction

data class CardInventoryWithTransactions(
    @Embedded val cardInventory: CardInventory,
    @Relation(
        parentColumn = "inventoryId",
        entityColumn = "inventoryId"
    )
    val transactions: List<Transaction>
)
