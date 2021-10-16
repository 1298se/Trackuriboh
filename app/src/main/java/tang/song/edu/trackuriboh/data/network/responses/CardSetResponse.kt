package tang.song.edu.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import tang.song.edu.trackuriboh.data.db.CardDatabase
import tang.song.edu.trackuriboh.data.db.entities.CardSet

data class CardSetResponse(
    val totalItems: Int,
    val errors: List<String>,
    val results: List<CardSetItem>,
) {
    data class CardSetItem(
        @SerializedName("groupId")
        val id: Long,
        val name: String,
        @SerializedName("abbreviation")
        val code: String?,
        @SerializedName("publishedOn")
        val publishedOn: String?
    ) : CardDatabase.DatabaseEntity<CardSet> {

        override fun toDatabaseEntity(): CardSet =
            CardSet(id, name, code, publishedOn)
    }
}
