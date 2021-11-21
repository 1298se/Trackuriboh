package sam.g.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.Condition

data class ConditionResponse(
    override val errors: List<String>,
    override val results: List<CardPrintingItem>,
) : BaseTCGPlayerResponse<ConditionResponse.CardPrintingItem> {

    data class CardPrintingItem(
        @SerializedName("conditionId")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("abbreviation")
        val abbreviation: String,
        @SerializedName("displayOrder")
        val order: Long?
    ) : AppDatabase.DatabaseEntity<Condition> {

        override fun toDatabaseEntity(): Condition =
            Condition(id, name, abbreviation, order)
    }
}