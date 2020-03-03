package tang.song.edu.yugiohcollectiontracker.data.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSetCrossRef
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Set

data class CardWithSets(
    @Embedded
    val card: Card,
    @Relation(
        parentColumn = "cardId",
        entityColumn = "setCode",
        associateBy = Junction(CardSetCrossRef::class)
    )
    val sets: List<Set>
)