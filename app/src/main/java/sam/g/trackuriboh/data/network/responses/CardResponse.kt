package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.RESULTS_FIELD_NAME

data class CardResponse(
    @SerializedName(ERROR_FIELD_NAME)
    override val errors: List<String>,
    @SerializedName(RESULTS_FIELD_NAME)
    override val results: List<CardItem>,
    val totalItems: Int,
) : BaseTCGPlayerResponse<CardResponse.CardItem> {

    data class CardItem (
        @SerializedName("productId")
        val id: Long,
        val name: String,
        val cleanName: String,
        val imageUrl: String?,
        @SerializedName("groupId")
        val setId: Long?,
        val extendedData: List<ExtendedDataItem>?,
        val skus: List<SkuResponse.SkuItem>?
    )

    data class ExtendedDataItem(
        val name: String,
        val displayName: String,
        val value: String,
    )
}
