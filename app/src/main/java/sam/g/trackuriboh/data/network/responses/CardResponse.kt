package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.types.ProductType


data class CardResponse(
    override val errors: List<String>,
    override val results: List<CardItem>,
    val totalItems: Int,
) : BaseTCGPlayerResponse<CardResponse.CardItem> {

    data class CardItem (
        @SerializedName("productId")
        val id: Long,
        val name: String,
        val cleanName: String?,
        val imageUrl: String?,
        @SerializedName("groupId")
        val setId: Long?,
        val extendedData: List<ExtendedDataItem>?,
        val skus: List<SkuResponse.SkuItem>?
    ) : AppDatabase.DatabaseEntity<Product> {

        override fun toDatabaseEntity(): Product =
            Product(
                id,
                name,
                ProductType.CARD,
                imageUrl,
                setId,
                extendedData
            )
    }

    data class ExtendedDataItem(
        val name: String,
        val displayName: String,
        val value: String,
    )
}
