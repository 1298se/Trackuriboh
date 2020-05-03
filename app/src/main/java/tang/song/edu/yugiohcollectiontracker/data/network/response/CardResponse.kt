package tang.song.edu.yugiohcollectiontracker.data.network.response

import com.google.gson.annotations.SerializedName

data class CardResponse(
    val id: Long,
    val name: String,
    val type: String,
    val desc: String?,
    val atk: Int?,
    val def: Int?,
    val level: Int?,
    val race: String?,
    val attribute: String?,
    val archetype: String?,
    val scale: Int?,
    @SerializedName("card_sets")
    val cardSetDetails: List<CardSetDetailResponse>?,
    @SerializedName("card_images")
    val cardImages: List<CardImageResponse>?,
    @SerializedName("card_prices")
    val cardPrices: List<CardPriceResponse>?
) {
    override fun equals(other: Any?): Boolean {
        if (other is CardResponse) {
            return id == other.id
        }

        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (desc?.hashCode() ?: 0)
        result = 31 * result + (atk ?: 0)
        result = 31 * result + (def ?: 0)
        result = 31 * result + (level ?: 0)
        result = 31 * result + (race?.hashCode() ?: 0)
        result = 31 * result + (attribute?.hashCode() ?: 0)
        result = 31 * result + (archetype?.hashCode() ?: 0)
        result = 31 * result + (scale ?: 0)
        result = 31 * result + (cardSetDetails?.hashCode() ?: 0)
        result = 31 * result + (cardImages?.hashCode() ?: 0)
        result = 31 * result + (cardPrices?.hashCode() ?: 0)
        return result
    }
}