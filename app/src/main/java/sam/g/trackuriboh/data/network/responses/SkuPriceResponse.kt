package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.RESULTS_FIELD_NAME

data class SkuPriceResponse(
    @SerializedName(ERROR_FIELD_NAME)
    override val errors: List<String>,
    @SerializedName(RESULTS_FIELD_NAME)
    override val results: List<SkuPriceResponseItem>,
) : BaseTCGPlayerResponse<SkuPriceResponse.SkuPriceResponseItem> {

    data class SkuPriceResponseItem(
        @SerializedName("skuId")
        val id: Long,
        // This value should be lowestBasePrice + lowestShippingPrice
        val lowestListingPrice: Double?,
        @SerializedName("lowPrice")
        val lowestBasePrice: Double?,
        @SerializedName("lowestShipping")
        val lowestShippingPrice: Double?,
        val marketPrice: Double?,
    )
}
