package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Sku

data class SkuResponse(
    override val errors: List<String>,
    override val results: List<SkuItem>,
) : BaseTCGPlayerResponse<SkuResponse.SkuItem> {

    data class SkuItem(
        @SerializedName("skuId")
        val id: Long,
        val productId: Long,
        val printingId: Long?,
        val conditionId: Long?,
    ) : AppDatabase.DatabaseEntity<Sku> {

        override fun toDatabaseEntity(): Sku =
            Sku(id, productId, printingId, conditionId)
    }
}
