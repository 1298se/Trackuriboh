package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.CardSet

data class CardSetResponse(
    val totalItems: Int,
    override val errors: List<String>,
    override val results: List<CardSetItem>,
) : BaseTCGPlayerResponse<CardSetResponse.CardSetItem> {

    data class CardSetItem(
        @SerializedName("groupId")
        val id: Long,
        val name: String,
        @SerializedName("abbreviation")
        val code: String?,
        @SerializedName("publishedOn")
        val releaseDate: String?
    ) : AppDatabase.DatabaseEntity<CardSet> {

        override fun toDatabaseEntity(): CardSet =
            CardSet(id, name, code, releaseDate)
    }
}
