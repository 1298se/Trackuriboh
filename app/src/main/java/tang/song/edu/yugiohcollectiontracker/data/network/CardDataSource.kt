package tang.song.edu.yugiohcollectiontracker.data.network

import tang.song.edu.yugiohcollectiontracker.data.models.MainDeckMonsterTypes
import javax.inject.Inject

class CardDataSource @Inject constructor(
    private val cardRetrofitService: CardRetrofitService
){
    suspend fun getAllCards() = cardRetrofitService.getAllCards()

    suspend fun getCards(type: MainDeckMonsterTypes) = cardRetrofitService.getCards(type.value)
}
