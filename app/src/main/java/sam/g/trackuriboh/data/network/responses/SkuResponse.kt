package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.RESULTS_FIELD_NAME

data class SkuResponse(
    @SerializedName(ERROR_FIELD_NAME)
    override val errors: List<String>,
    @SerializedName(RESULTS_FIELD_NAME)
    override val results: List<SkuItem>,
) : BaseTCGPlayerResponse<SkuResponse.SkuItem> {

    data class SkuItem(
        @SerializedName("skuId")
        val id: Long,
        val productId: Long,
        val printingId: Long?,
        val conditionId: Long?,
    )
}
