package tang.song.edu.yugiohcollectiontracker.data.repository

import tang.song.edu.yugiohcollectiontracker.data.network.SetDataSource
import tang.song.edu.yugiohcollectiontracker.data.network.response.Resource
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

    suspend fun getAllSets(): Resource<List<SetResponse>> {
        return Resource.success(setDataSource.getAllSets())
    }
}
