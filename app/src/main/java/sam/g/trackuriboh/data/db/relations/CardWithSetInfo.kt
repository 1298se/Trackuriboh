package sam.g.trackuriboh.data.db.relations

import sam.g.trackuriboh.data.db.entities.Product

data class CardWithSetInfo(
    val product: Product,
    val sets: List<CardSetInfo>
)