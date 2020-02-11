package tang.song.edu.yugiohcollectiontracker.network.response

import com.google.gson.annotations.SerializedName

data class CardModel(
    val id: Long,
    val name: String,
    val type: String,
    val desc: String,
    val atk: Int,
    val def: Int,
    val level: Int,
    val race: String,
    val attribute: String,
    @SerializedName("card_sets")
    val cardSets: List<CardSetModel>,
    @SerializedName("card_images")
    val cardImages: List<CardImageModel>,
    @SerializedName("card_prices")
    val cardPrices: List<CardPriceModel>
)