package sam.g.trackuriboh.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.entities.Sku

data class ProductWithSkus(
    @Embedded val product: Product,
    @Relation(
        entity = Sku::class,
        parentColumn = "id",
        entityColumn = "productId"
    )
    val skus: List<SkuWithConditionAndPrinting>
)
