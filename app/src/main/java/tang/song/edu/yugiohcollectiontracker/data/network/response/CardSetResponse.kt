package tang.song.edu.yugiohcollectiontracker.data.network.response

import com.google.gson.annotations.SerializedName

data class CardSetResponse(
    @SerializedName("set_name")
    val setName: String,
    @SerializedName("set_code")
    val setCode: String,
    @SerializedName("num_of_cards")
    val size: Long,
    @SerializedName("tcg_date")
    val releaseDate: String
)
