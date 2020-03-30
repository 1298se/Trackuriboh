package tang.song.edu.yugiohcollectiontracker.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.SetResponse

interface CardRetrofitService {

    @GET("cardinfo.php")
    suspend fun getAllCards(): Response<List<CardResponse>>

    @GET("cardsets.php")
    suspend fun getAllSets(): Response<List<SetResponse>>

    @GET("cardinfo.php")
    suspend fun getCards(@Query("type") type: List<String>): Response<List<CardResponse>>
}
