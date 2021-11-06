package sam.g.trackuriboh.data.db.relations

import sam.g.trackuriboh.data.db.entities.Card

data class CardWithSetInfo(
    val card: Card,
    val sets: List<CardSetInfo>
)