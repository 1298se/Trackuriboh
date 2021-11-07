package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Sku

data class SkuResponse(
    val errors: List<String>,
    val results: List<SkuItem>,
) {
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
