package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName

data class ProductPriceResponse(
    @SerializedName(BaseTCGPlayerResponse.ERROR_FIELD_NAME)
    override val errors: List<String>,
    @SerializedName(BaseTCGPlayerResponse.RESULTS_FIELD_NAME)
    override val results: List<ProductPriceResponseItem>,
) : BaseTCGPlayerResponse<ProductPriceResponse.ProductPriceResponseItem> {

    data class ProductPriceResponseItem(
        @SerializedName("productId")
        val id: Long,
        val marketPrice: Double?,
        val subTypeName: String,
    )
}