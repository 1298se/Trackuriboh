package sam.g.trackuriboh.workers

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.ProductLocalCache
import sam.g.trackuriboh.data.network.responses.CardResponse
import sam.g.trackuriboh.data.network.responses.CardSetResponse
import sam.g.trackuriboh.data.network.services.CardSetApiService
import sam.g.trackuriboh.data.network.services.CatalogApiService
import sam.g.trackuriboh.data.network.services.ProductApiService

/**
 * Worker to fetch all the necessary data from TCGPlayer API
 */
@HiltWorker
class DatabaseSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cardSetApiService: CardSetApiService,
    private val productApiService: ProductApiService,
    private val catalogApiService: CatalogApiService,
    private val productLocalCache: ProductLocalCache,
) : CoroutineWorker(appContext, workerParams) {

    private val notificationBuilder by lazy {
        createNotificationBuilder(
            channelName = appContext.getString(R.string.database_worker_channel_name),
            notificationTitle = appContext.getString(R.string.database_worker_sync_notification_title),
            cancelText = appContext.getString(R.string.lbl_cancel),
            showProgress = true
        )
    }


    companion object {
        const val WORKER_TAG = "DatabaseSyncWorker"
        const val Progress = "DatabaseSyncProgress"
        private const val NOTIFICATION_ID = 1

        private const val PAGINATION_LIMIT_SIZE = 100
        private const val MAX_PARALLEL_REQUESTS = 10
    }

    override suspend fun doWork(): Result  = withContext(Dispatchers.Default){
        try {
            val cardResponse = productApiService.getCards()
            val cardSetResponse = cardSetApiService.getSets()
            val cardRarityResponse = catalogApiService.getCardRarities()
            val printingResponse = catalogApiService.getPrintings()
            val conditionResponse = catalogApiService.getConditions()

            productLocalCache.run {
                insertCardRarities(cardRarityResponse.results.map { it.toDatabaseEntity() })
                insertPrintings(printingResponse.results.map { it.toDatabaseEntity() })
                insertConditions(conditionResponse.results.map { it.toDatabaseEntity() })
            }

            paginateAndPopulateDatabase(
                ::getCardSetList,
                ::insertCardSets,
                cardSetResponse.totalItems,
            )

            paginateAndPopulateDatabase(
                ::getCardList,
                ::insertCards,
                cardResponse.totalItems,
            )

            // TODO: Created notification alerting user that sync is complete
            Result.success()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, notificationBuilder.build(),
        )
    }

    private suspend fun getCardList(offset: Int, limit: Int): List<CardResponse.CardItem> =
        productApiService.getCards(offset, limit).results

    private suspend fun getCardSetList(offset: Int, limit: Int): List<CardSetResponse.CardSetItem> =
        cardSetApiService.getSets(offset, limit).results

    private suspend fun insertCardSets(cardSets: List<CardSetResponse.CardSetItem>): List<Long> =
        productLocalCache.insertCardSets(cardSets.map { it.toDatabaseEntity() })

    private suspend fun insertCards(cards: List<CardResponse.CardItem>): List<Long> {
        val result = productLocalCache.insertProducts(cards.map { it.toDatabaseEntity() })
        cards.forEach { cardItem -> cardItem.skus?.let {
            productLocalCache.insertProductSkus(cardItem.skus.map { it.toDatabaseEntity() })
        } }

        return result
    }

    private suspend fun <T> paginateAndPopulateDatabase(
        apiServiceCall: suspend (offset: Int, limit: Int) -> List<T>,
        databaseInsert: suspend (List<T>) -> Any?,
        totalCount: Int,
    ) {

        // Each bach should make MAX_PARALLEL_REQUESTS number of requests and fetch PAGINATION_LIMIT_SIZE number of items
        val batchOffsetIncrements = minOf(totalCount, MAX_PARALLEL_REQUESTS * PAGINATION_LIMIT_SIZE)

        for (batchOffset in 0 until totalCount step batchOffsetIncrements) {
            coroutineScope {
                val requestBatch = (
                        batchOffset until minOf(batchOffset + batchOffsetIncrements, totalCount)
                        step PAGINATION_LIMIT_SIZE
                    ).map { curOffset ->
                        async {
                        val itemList = apiServiceCall(curOffset, PAGINATION_LIMIT_SIZE)

                        databaseInsert(itemList)
                    }
                }

                requestBatch.awaitAll()
                updateProgress((batchOffset.toDouble() / totalCount * 100).toInt())
            }
        }
    }

    /**
     * Progress should be normalized to a percentage [0, 100]
     */
    private suspend fun updateProgress(progress: Int) {
        NotificationManagerCompat.from(applicationContext).apply {
            notificationBuilder.setProgress(MAX_PROGRESS, progress, false)
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }

        setProgress(workDataOf(Progress to progress))
    }
}
