package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.RESULTS_FIELD_NAME
import sam.g.trackuriboh.data.types.ProductType

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
    ) : AppDatabase.DatabaseEntity<Product> {

        override fun toDatabaseEntity(): Product =
            Product(
                id = id,
                name = name,
                cleanName = cleanName,
                productType = ProductType.CARD,
                imageUrl = imageUrl,
                setId = setId,
                extendedData = extendedData
            )
    }

    data class ExtendedDataItem(
        val name: String,
        val displayName: String,
        val value: String,
    )
}
