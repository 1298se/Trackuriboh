package tang.song.edu.yugiohcollectiontracker.data.network

import retrofit2.Response
import retrofit2.http.GET
import tang.song.edu.yugiohcollectiontracker.data.network.response.AllCardsResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetResponse

interface CardRetrofitService {

    @GET("cardinfo.php")
    suspend fun getAllCards(): Response<AllCardsResponse>

    @GET("cardsets.php")
    suspend fun getAllSets(): Response<List<CardSetResponse>>
}
