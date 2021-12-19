package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.CardRarity
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.RESULTS_FIELD_NAME

data class CardRarityResponse(
    @SerializedName(ERROR_FIELD_NAME)
    override val errors: List<String>,
    @SerializedName(RESULTS_FIELD_NAME)
    override val results: List<CardRarityItem>,
) : BaseTCGPlayerResponse<CardRarityResponse.CardRarityItem> {

    data class CardRarityItem(
        @SerializedName("rarityId")
        val id: Long,
        @SerializedName("displayText")
        val name: String,
        @SerializedName("dbValue")
        val simpleName: String,
    ) : AppDatabase.DatabaseEntity<CardRarity> {

        override fun toDatabaseEntity(): CardRarity {
            // Common / Short Print is very misleading... I thought it meant the common was short printed.
            // Let's just use Common
            val rarityName = if (name == "Common / Short Print" && simpleName == "Common") simpleName else name

            return CardRarity(id, rarityName)
        }
    }
}