package tang.song.edu.yugiohcollectiontracker.network

import retrofit2.http.GET
import tang.song.edu.yugiohcollectiontracker.network.response.CardModel

interface CardRetrofitService {

    @GET("cardinfo.php")
    suspend fun getAllCards(): List<CardModel>
}
