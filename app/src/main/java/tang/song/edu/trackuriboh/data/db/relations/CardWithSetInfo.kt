package tang.song.edu.trackuriboh.data.db.relations

import tang.song.edu.trackuriboh.data.db.entities.Card

data class CardWithSetInfo(
    val card: Card,
    val sets: List<CardSetInfo>
)