package tang.song.edu.yugiohcollectiontracker.network

import android.util.Log
import tang.song.edu.yugiohcollectiontracker.network.response.CardModel
import tang.song.edu.yugiohcollectiontracker.network.response.Resource
import tang.song.edu.yugiohcollectiontracker.network.response.ResponseHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val cardDataSource: CardDataSource,
    private val responseHandler: ResponseHandler
) {
    suspend fun getAllCards(): Resource<List<CardModel>> {
        Log.d("TAG", "repository@"+ this.hashCode() + " cardDataSource@" + cardDataSource.hashCode() + " responseHandler@" + responseHandler)
        return try {
            responseHandler.handleSuccess(cardDataSource.getAllCards())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}
