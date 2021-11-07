package sam.g.trackuriboh.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product

data class ProductWithSetInfo(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "setId",
        entityColumn = "id",
    )
    val cardSet: CardSet
)
