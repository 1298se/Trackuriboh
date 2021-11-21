package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName

data class SkuPriceResponse(
    override val errors: List<String>,
    override val results: List<SkuPriceResponseItem>,
) : BaseTCGPlayerResponse<SkuPriceResponse.SkuPriceResponseItem> {

    data class SkuPriceResponseItem(
        @SerializedName("skuId")
        val id: Long,
        val lowestListingPrice: Double?,
        @SerializedName("lowestShipping")
        val lowestShippingPrice: Double?,
        val marketPrice: Double?,
    )
}
