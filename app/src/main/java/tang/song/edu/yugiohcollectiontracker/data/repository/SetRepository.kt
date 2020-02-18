package tang.song.edu.yugiohcollectiontracker.data.repository

import android.util.Log
import tang.song.edu.yugiohcollectiontracker.network.SetDataSource
import tang.song.edu.yugiohcollectiontracker.network.response.Resource
import tang.song.edu.yugiohcollectiontracker.network.response.ResponseHandler
import tang.song.edu.yugiohcollectiontracker.network.response.SetResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetRepository @Inject constructor(
    private val setDataSource: SetDataSource,
    private val responseHandler: ResponseHandler
) {
    companion object {
        private const val TAG = "CardRepository"
    }

    suspend fun getAllSets(): Resource<List<SetResponse>> {
        Log.d(
            TAG,
            "repository@" + this.hashCode() + " cardDataSource@" + setDataSource.hashCode() + " responseHandler@" + responseHandler
        )
        return try {
            responseHandler.handleSuccess(setDataSource.getAllSets())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}
