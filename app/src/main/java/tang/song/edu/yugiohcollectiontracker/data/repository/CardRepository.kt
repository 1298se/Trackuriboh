package tang.song.edu.yugiohcollectiontracker.data.repository

import android.util.Log
import tang.song.edu.yugiohcollectiontracker.network.CardDataSource
import tang.song.edu.yugiohcollectiontracker.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.network.response.Resource
import tang.song.edu.yugiohcollectiontracker.network.response.ResponseHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val cardDataSource: CardDataSource,
    private val responseHandler: ResponseHandler
) {
    companion object {
        private const val TAG = "CardRepository"
    }

    suspend fun getAllCards(): Resource<List<CardResponse>> {
        Log.d(
            TAG,
            "repository@" + this.hashCode() + " cardDataSource@" + cardDataSource.hashCode() + " responseHandler@" + responseHandler
        )
        return try {
            responseHandler.handleSuccess(cardDataSource.getAllCards())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}
