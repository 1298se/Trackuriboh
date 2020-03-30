package tang.song.edu.yugiohcollectiontracker.data.repository

import retrofit2.Response
import tang.song.edu.yugiohcollectiontracker.data.SetDataSource
import tang.song.edu.yugiohcollectiontracker.data.network.response.SetResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetRepository @Inject constructor(
    private val setDataSource: SetDataSource
) {
    companion object {
        private const val TAG = "CardRepository"
    }

    suspend fun getAllSets(): Response<List<SetResponse>> {
        return setDataSource.getAllSets()
    }
}
