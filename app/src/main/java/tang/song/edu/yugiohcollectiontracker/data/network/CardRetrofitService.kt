package tang.song.edu.yugiohcollectiontracker.data.network

import retrofit2.http.GET
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardListResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetResponse

interface CardRetrofitService {

    @GET("cardinfo.php")
    suspend fun getAllCards(): CardListResponse

    @GET("cardsets.php")
    suspend fun getAllSets(): List<CardSetResponse>
}
