package sam.g.trackuriboh.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import sam.g.trackuriboh.data.db.entities.Condition
import sam.g.trackuriboh.data.db.entities.Printing
import sam.g.trackuriboh.data.db.entities.Sku

data class SkuWithConditionAndPrinting(
    @Embedded val sku: Sku,
    @Relation(
        parentColumn = "printingId",
        entityColumn = "id",
    )
    val printing: Printing?,
    @Relation(
        parentColumn = "conditionId",
        entityColumn = "id",
    )
    val condition: Condition?,
)
