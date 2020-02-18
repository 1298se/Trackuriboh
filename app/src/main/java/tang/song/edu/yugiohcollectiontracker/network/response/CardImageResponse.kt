package tang.song.edu.yugiohcollectiontracker.network.response

import com.google.gson.annotations.SerializedName

data class CardImageResponse(
    val id: Long,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("image_url_small")
    val imageUrlSmall: String
)
