package sam.g.trackuriboh.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CardWithSetInfo(
    @Embedded val card: Card,
    @Relation(
        parentColumn = "setId",
        entityColumn = "id",
    )
    val cardSet: CardSet
)
