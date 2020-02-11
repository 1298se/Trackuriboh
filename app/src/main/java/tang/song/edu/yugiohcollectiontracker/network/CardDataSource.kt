package tang.song.edu.yugiohcollectiontracker.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardDataSource @Inject constructor(
    private val cardRetrofitService: CardRetrofitService
){
    suspend fun getAllCards() = cardRetrofitService.getAllCards()
}
