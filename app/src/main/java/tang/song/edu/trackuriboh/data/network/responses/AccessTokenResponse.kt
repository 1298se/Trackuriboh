package tang.song.edu.trackuriboh.data.network.responses

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token")
    val token: String,
    @SerializedName(".expires")
    val expiry: String
)
