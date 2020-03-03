package tang.song.edu.yugiohcollectiontracker.data.network.response

import com.google.gson.annotations.SerializedName

data class CardResponse(
    val id: Long,
    val name: String,
    val type: String,
    val desc: String,
    val atk: Int,
    val def: Int,
    val level: Int,
    val race: String,
    val attribute: String,
    val archetype: String,
    val scale: Int,
    @SerializedName("card_sets")
    val cardSets: List<CardSetResponse>,
    @SerializedName("card_images")
    val cardImages: List<CardImageResponse>,
    @SerializedName("card_prices")
    val cardPrices: List<CardPriceResponse>
) {
    override fun equals(other: Any?): Boolean {
        if (other is CardResponse) {
            return id == other.id
        }

        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}