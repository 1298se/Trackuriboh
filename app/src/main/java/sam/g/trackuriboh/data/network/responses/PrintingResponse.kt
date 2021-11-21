package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Printing

data class PrintingResponse(
    override val errors: List<String>,
    override val results: List<CardPrintingItem>,
) : BaseTCGPlayerResponse<PrintingResponse.CardPrintingItem> {

    data class CardPrintingItem(
        @SerializedName("printingId")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("displayOrder")
        val order: Long?
    ) : AppDatabase.DatabaseEntity<Printing> {

        override fun toDatabaseEntity(): Printing =
            Printing(id, name, order)
    }
}