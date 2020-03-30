package tang.song.edu.yugiohcollectiontracker.data.repository

import retrofit2.Response
import tang.song.edu.yugiohcollectiontracker.data.CardDataSource
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val cardDataSource: CardDataSource
) {

    suspend fun getAllCards(): Response<List<CardResponse>> {
        return cardDataSource.getAllCards()
    }
}
