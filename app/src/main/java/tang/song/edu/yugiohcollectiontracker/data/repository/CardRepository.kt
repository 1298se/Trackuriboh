package tang.song.edu.yugiohcollectiontracker.data.repository

import kotlinx.coroutines.flow.Flow
import tang.song.edu.yugiohcollectiontracker.data.db.dao.CardDao
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.models.MainDeckMonsterTypes
import tang.song.edu.yugiohcollectiontracker.data.network.CardDataSource
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.PagedNetworkBoundResource
import tang.song.edu.yugiohcollectiontracker.data.network.response.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val cardDataSource: CardDataSource,
    private val cardDao: CardDao
) {
    companion object {
        private const val TAG = "CardRepository"
    }

    fun getAllCards(): Flow<Resource<List<Card>>> {
        return object : PagedNetworkBoundResource<List<Card>, List<CardResponse>>() {
            override suspend fun saveCallResult(item: List<CardResponse>?) {
                if (item == null) {
                    return
                }
            }

            override suspend fun shouldFetch(data: List<Card>?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override suspend fun loadFromDb(): List<Card> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override suspend fun createCall(): List<CardResponse> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }.asFlow()
    }


    suspend fun getCards(type: MainDeckMonsterTypes): Resource<List<CardResponse>> {
        return Resource.success(cardDataSource.getCards(type))
    }
}
