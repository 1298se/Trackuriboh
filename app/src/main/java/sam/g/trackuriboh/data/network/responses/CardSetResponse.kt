package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.RESULTS_FIELD_NAME

data class CardSetResponse(
    @SerializedName(ERROR_FIELD_NAME)
    override val errors: List<String>,
    @SerializedName(RESULTS_FIELD_NAME)
    override val results: List<CardSetItem>,
    val totalItems: Int,
) : BaseTCGPlayerResponse<CardSetResponse.CardSetItem> {

    data class CardSetItem(
        @SerializedName("groupId")
        val id: Long,
        val name: String,
        @SerializedName("abbreviation")
        val code: String?,
        @SerializedName("publishedOn")
        val releaseDate: String?,
        @SerializedName("modifiedOn")
        val modifiedDate: String?,
    )
}
