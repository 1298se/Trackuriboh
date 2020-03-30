package tang.song.edu.yugiohcollectiontracker.data

import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService
import javax.inject.Inject

class SetDataSource @Inject constructor(
    private val cardRetrofitService: CardRetrofitService
) {
    suspend fun getAllSets() = cardRetrofitService.getAllSets()
}
