package tang.song.edu.yugiohcollectiontracker.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardInventory
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Transaction

data class CardInventoryWithTransactions(
    @Embedded val cardInventory: CardInventory,
    @Relation(
        parentColumn = "inventoryId",
        entityColumn = "inventoryId"
    )
    val transactions: List<Transaction>
)
