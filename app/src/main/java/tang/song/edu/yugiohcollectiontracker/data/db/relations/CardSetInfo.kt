package tang.song.edu.yugiohcollectiontracker.data.db.relations

data class CardSetInfo(
    val setCode: String,
    val setName: String,
    val rarity: String?,
    val releaseDate: String?
)
