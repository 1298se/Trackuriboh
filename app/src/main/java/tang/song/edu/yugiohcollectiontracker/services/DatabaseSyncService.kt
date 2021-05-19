package tang.song.edu.yugiohcollectiontracker.services

import android.util.Log
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tang.song.edu.yugiohcollectiontracker.data.db.CardLocalCache
import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService
import javax.inject.Inject

@ViewModelScoped
class DatabaseSyncService @Inject constructor(
    private val cardRetrofitService: CardRetrofitService,
    private val cardLocalCache: CardLocalCache,
) {
    val databaseSyncState: StateFlow<DatabaseSyncState>
        get() = _databaseSyncStateFlow

    private val _databaseSyncStateFlow: MutableStateFlow<DatabaseSyncState> = MutableStateFlow(DatabaseSyncState.IDLE)

    companion object {
        private val TAG = DatabaseSyncService::class.java.name
    }

    suspend fun syncDatabase() {
        try {

            coroutineScope {
                _databaseSyncStateFlow.value = DatabaseSyncState.LOADING
                val cardListRequest = async { cardRetrofitService.getAllCards() }
                val cardSetListRequest = async { cardRetrofitService.getAllSets() }

                val cardListResponse = cardListRequest.await()
                val cardSetListResponse = cardSetListRequest.await()

                Log.d(TAG, "cards from api: " + cardListResponse.data.size)
                Log.d(TAG, "cardSets from api: " + cardSetListResponse.size)

                cardLocalCache.clearDatabase()
                cardLocalCache.populateDatabase(TAG, cardListResponse.data, cardSetListResponse)
            }

            _databaseSyncStateFlow.value = DatabaseSyncState.SUCCESS
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            _databaseSyncStateFlow.value = DatabaseSyncState.FAILURE(throwable.message)
        }
    }

    sealed class DatabaseSyncState {
        object IDLE: DatabaseSyncState()
        object LOADING : DatabaseSyncState()
        object SUCCESS : DatabaseSyncState()
        data class FAILURE(val msg: String?) : DatabaseSyncState()
    }
}
