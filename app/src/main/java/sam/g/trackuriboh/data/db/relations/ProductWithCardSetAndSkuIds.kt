package sam.g.trackuriboh.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.entities.Sku

data class ProductWithCardSetAndSkuIds(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "setId",
        entityColumn = "id",
    )
    val cardSet: CardSet,
    @Relation(
        entity = Sku::class,
        parentColumn = "id",
        entityColumn = "productId",
        projection = ["id"]
    )
    val skuIds: List<Long>,
)
