package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.RESULTS_FIELD_NAME

data class PrintingResponse(
    @SerializedName(ERROR_FIELD_NAME)
    override val errors: List<String>,
    @SerializedName(RESULTS_FIELD_NAME)
    override val results: List<CardPrintingItem>,
) : BaseTCGPlayerResponse<PrintingResponse.CardPrintingItem> {

    data class CardPrintingItem(
        @SerializedName("printingId")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("displayOrder")
        val order: Int?
    )
}