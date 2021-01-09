package tang.song.edu.yugiohcollectiontracker.data.network.response

import com.google.gson.annotations.SerializedName

data class CardPriceResponse(
    @SerializedName("cardmarket_price")
    val cardMarketPrice: String,
    @SerializedName("tcgplayer_price")
    val tcgPlayerPrice: String,
    @SerializedName("ebay_price")
    val ebayPrice: String,
    @SerializedName("amazon_price")
    val amazonPrice: String,
    @SerializedName("coolstuffinc_price")
    val coolstuffincPrice: String,
)
