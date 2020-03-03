package tang.song.edu.yugiohcollectiontracker.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.SetResponse

interface CardRetrofitService {

    @GET("cardinfo.php")
    suspend fun getAllCards(): List<CardResponse>

    @GET("cardsets.php")
    suspend fun getAllSets(): List<SetResponse>

    @GET("cardinfo.php")
    suspend fun getCards(@Query("type") type: List<String>): List<CardResponse>
}
