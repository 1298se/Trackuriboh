package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName

data class SkuPriceResponse(
    val errors: List<String>,
    val results: List<SkuPriceResponseItem>,
) {
    data class SkuPriceResponseItem(
        @SerializedName("skuId")
        val id: Long,
        val lowestListingPrice: Double?,
        @SerializedName("lowestShipping")
        val lowestShippingPrice: Double?,
        val marketPrice: Double?,
    )
}
