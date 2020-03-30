package tang.song.edu.yugiohcollectiontracker.data

import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService
import javax.inject.Inject

class CardDataSource @Inject constructor(
    private val cardRetrofitService: CardRetrofitService
){
    suspend fun getAllCards() = cardRetrofitService.getAllCards()
}
