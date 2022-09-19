package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.RESULTS_FIELD_NAME

data class ConditionResponse(
    @SerializedName(ERROR_FIELD_NAME)
    override val errors: List<String>,
    @SerializedName(RESULTS_FIELD_NAME)
    override val results: List<CardConditionItem>,
) : BaseTCGPlayerResponse<ConditionResponse.CardConditionItem> {

    data class CardConditionItem(
        @SerializedName("conditionId")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("abbreviation")
        val abbreviation: String,
        @SerializedName("displayOrder")
        val order: Int?
    )
}