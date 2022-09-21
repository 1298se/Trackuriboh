package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
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
    )
}