package tang.song.edu.yugiohcollectiontracker.network

import retrofit2.http.GET
import tang.song.edu.yugiohcollectiontracker.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.network.response.SetResponse

interface CardRetrofitService {

    @GET("cardinfo.php")
    suspend fun getAllCards(): List<CardResponse>

    @GET("cardsets.php")
    suspend fun getAllSets(): List<SetResponse>
}
