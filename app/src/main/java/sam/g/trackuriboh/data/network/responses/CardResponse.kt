package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.CardDatabase
import sam.g.trackuriboh.data.db.entities.Card


data class CardResponse(
    val totalItems: Int,
    val errors: List<String>,
    val results: List<CardItem>,
) {
    data class CardItem (
        @SerializedName("productId")
        val id: Long,
        val name: String,
        val cleanName: String?,
        val imageUrl: String?,
        @SerializedName("groupId")
        val setId: Long?,
        val extendedData: List<ExtendedDataItem>
    ) : CardDatabase.DatabaseEntity<Card> {

        override fun toDatabaseEntity(): Card =
            Card(
                id,
                name,
                cleanName,
                imageUrl,
                setId,
                extendedData
            )
    }

    data class ExtendedDataItem(
        val name: String,
        val displayName: String,
        val value: String,
    )
}
