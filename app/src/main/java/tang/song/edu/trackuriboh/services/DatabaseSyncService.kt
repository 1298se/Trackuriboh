package tang.song.edu.trackuriboh.services

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import tang.song.edu.trackuriboh.data.db.CardDatabase
import tang.song.edu.trackuriboh.data.db.CardLocalCache
import tang.song.edu.trackuriboh.data.network.responses.CardResponse
import tang.song.edu.trackuriboh.data.network.responses.CardSetResponse
import tang.song.edu.trackuriboh.data.network.services.CardApiService
import tang.song.edu.trackuriboh.data.network.services.CardSetApiService
import javax.inject.Inject

private const val PAGINATION_LIMIT_SIZE = 100
private const val MAX_PARALLEL_REQUESTS = 20
private const val REQUEST_INTERVAL_DELAY = 2000L

@ViewModelScoped
class DatabaseSyncService @Inject constructor(
    private val cardSetApiService: CardSetApiService,
    private val cardApiService: CardApiService,
    private val cardLocalCache: CardLocalCache,
) {
    suspend fun syncDatabase(): Flow<DatabaseSyncState> = flow {
        try {
            emit(DatabaseSyncState.LOADING(0))

            val cardResponse = cardApiService.getCards()
            val cardSetResponse = cardSetApiService.getSets()

            paginateAndPopulateDatabase(
                ::getCardSetList,
                cardLocalCache::insertCardSets,
                cardSetResponse.totalItems,
                this@flow,
            )

            paginateAndPopulateDatabase(
                ::getCardList,
                cardLocalCache::insertCards,
                cardResponse.totalItems,
                this@flow,
            )

            emit(DatabaseSyncState.SUCCESS)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            emit(DatabaseSyncState.FAILURE(throwable.message))
        } finally {
            emit(DatabaseSyncState.IDLE)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getCardList(offset: Int, limit: Int): List<CardResponse.CardItem> =
        cardApiService.getCards(offset, limit).results

    private suspend fun getCardSetList(offset: Int, limit: Int): List<CardSetResponse.CardSetItem> =
        cardSetApiService.getSets(offset, limit).results

    private suspend fun <T : CardDatabase.DatabaseEntity<E>, E> paginateAndPopulateDatabase(
        apiServiceCall: suspend (offset: Int, limit: Int) -> List<T>,
        databaseInsert: suspend (List<E>) -> List<Long>,
        totalCount: Int,
        flow: FlowCollector<DatabaseSyncState>,
    ) {

        // Each bach should make MAX_PARALLEL_REQUESTS number of requests and fetch PAGINATION_LIMIT_SIZE number of items
        // If totalCount is smaller
        val batchOffsetIncrements = minOf(totalCount, MAX_PARALLEL_REQUESTS * PAGINATION_LIMIT_SIZE)

        for (batchOffset in 0 until totalCount step batchOffsetIncrements) {
            coroutineScope {
                val requestBatch = (
                        batchOffset until minOf(batchOffset + batchOffsetIncrements, totalCount)
                        step PAGINATION_LIMIT_SIZE
                    ).map { curOffset ->
                        async {
                        val itemList = apiServiceCall(curOffset, PAGINATION_LIMIT_SIZE)

                        databaseInsert(itemList.map { it.toDatabaseEntity() })
                    }
                }

                requestBatch.awaitAll()
                delay(REQUEST_INTERVAL_DELAY)
                flow.emit(DatabaseSyncState.LOADING((batchOffset.toDouble() / totalCount * 100).toInt()))
            }
        }
    }

    sealed class DatabaseSyncState {
        object IDLE : DatabaseSyncState()
        data class LOADING(val progress: Int) : DatabaseSyncState()
        object SUCCESS : DatabaseSyncState()
        data class FAILURE(val msg: String?) : DatabaseSyncState()
    }
}
