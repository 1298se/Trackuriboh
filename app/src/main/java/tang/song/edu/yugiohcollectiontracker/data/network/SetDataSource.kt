package tang.song.edu.yugiohcollectiontracker.data.network

import javax.inject.Inject

class SetDataSource @Inject constructor(
    private val cardRetrofitService: CardRetrofitService
) {
    suspend fun getAllSets() = cardRetrofitService.getAllSets()
}
