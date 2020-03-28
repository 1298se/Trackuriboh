package tang.song.edu.yugiohcollectiontracker.data.repository

import tang.song.edu.yugiohcollectiontracker.data.models.MainDeckMonsterTypes
import tang.song.edu.yugiohcollectiontracker.data.network.CardDataSource
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val cardDataSource: CardDataSource
) {

    suspend fun getAllCards(): Resource<List<CardResponse>> {
        return Resource.success(cardDataSource.getAllCards())
    }


    suspend fun getCards(type: MainDeckMonsterTypes): Resource<List<CardResponse>> {
        return Resource.success(cardDataSource.getCards(type))
    }
}
