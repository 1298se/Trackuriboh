package sam.g.trackuriboh.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.CatalogRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT
import java.util.*

/**
 * Worker to fully download all necessary data from TCGPlayer API
 */
@HiltWorker
class DatabaseDownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cardSetRepository: CardSetRepository,
    private val productRepository: ProductRepository,
    private val catalogRepository: CatalogRepository,
    private val appDatabase: AppDatabase,
    private val sharedPreferences: SharedPreferences,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORKER_TAG = "DatabaseDownloadWorker"
        const val Progress = "DatabaseDownloadProgress"
    }

    private val progressNotificationBuilder by lazy {
        createNotificationBuilder(
            context = applicationContext,
            channelId = DB_SYNC_NOTIFICATION_CHANNEL_ID,
            notificationTitle = appContext.getString(R.string.database_download_worker_notification_title),
            cancelIntent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id),
            showProgress = true,
            ongoing = true,
            autoCancel = false
        )
    }

    private val stateNotificationBuilder by lazy {
        createNotificationBuilder(
            context = applicationContext,
            channelId = DB_SYNC_NOTIFICATION_CHANNEL_ID,
            notificationTitle = appContext.getString(R.string.database_download_worker_notification_title),
        )
    }

    override suspend fun doWork(): Result  = withContext(Dispatchers.Default){
        try {
            updateProgress(0)

            appDatabase.clearCardDatabaseTables()

            val cardRarityResponse = catalogRepository.fetchCardRarities().getResponseOrThrow()
            val printingResponse = catalogRepository.fetchProductPrintings().getResponseOrThrow()
            val conditionResponse = catalogRepository.fetchProductConditions().getResponseOrThrow()

            with(productRepository) {
                insertCardRarities(cardRarityResponse.results.map { it.toDatabaseEntity() })
                insertPrintings(printingResponse.results.map { it.toDatabaseEntity() })
                insertConditions(conditionResponse.results.map { it.toDatabaseEntity() })
            }

            downloadDatabase()

            with(sharedPreferences.edit()) {
                putLong(DatabaseUpdateWorker.DATABASE_LAST_UPDATED_DATE, Date().time)
                commit()
            }

            with(NotificationManagerCompat.from(applicationContext)) {
                stateNotificationBuilder.setContentText(applicationContext.getString(R.string.database_download_success))
                notify(DB_SYNC_STATE_NOTIFICATION_ID, stateNotificationBuilder.build())
            }

            Result.success()
        } catch (exception: Exception) {

            // Once a coroutine is cancelled, it cannot suspend anymore, hence we must use
            // a NonCancellable CoroutineContext to perform the database cleanup.
            withContext(NonCancellable) {
                // We should probably clear the tables or else the update won't be able to work properly,
                // since the update checker will not correct if we were in the middle of downloading a
                // particular set that has already been released
                appDatabase.clearCardDatabaseTables()
            }

            with(NotificationManagerCompat.from(applicationContext)) {
                val contentString = applicationContext.getString(
                    if (exception.cause is CancellationException)
                        R.string.database_download_cancelled else R.string.database_download_failed
                )

                stateNotificationBuilder.setContentText(contentString)
                notify(DB_SYNC_STATE_NOTIFICATION_ID, stateNotificationBuilder.build())
            }

            Result.failure()
        } finally {
            NotificationManagerCompat.from(applicationContext).cancel(DB_SYNC_PROGRESS_NOTIFICATION_ID)
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            DB_SYNC_PROGRESS_NOTIFICATION_ID, progressNotificationBuilder.build(),
        )
    }

    /**
     * Progress should be normalized to a percentage [0, 100]
     */
    private suspend fun updateProgress(progress: Int, maxProgress: Int = MAX_PROGRESS) {
        NotificationManagerCompat.from(applicationContext).apply {
            progressNotificationBuilder.apply {
                setProgress(maxProgress, progress, false)
            }
            notify(DB_SYNC_PROGRESS_NOTIFICATION_ID, progressNotificationBuilder.build())
        }

        setProgress(workDataOf(Progress to progress))
    }

    private suspend fun downloadDatabase() {
        // Get the total count for pagination
        val cardSetResponse = cardSetRepository.fetchCardSets(limit = 1).getResponseOrThrow()
        val productResponse = productRepository.fetchProducts(limit = 1).getResponseOrThrow()

        // Offset and total items for updating progress
        var totalOffset = 0
        val totalItems = cardSetResponse.totalItems + productResponse.totalItems

        paginate(
            totalCount = cardSetResponse.totalItems,
            paginationSize = DEFAULT_QUERY_LIMIT,
            paginate = { offset, paginationSize -> cardSetRepository.fetchCardSets(offset, paginationSize).getResponseOrThrow().results },
        ) { offset, cardSets ->
            updateProgress(((totalOffset + offset.toDouble()) / totalItems * 100).toInt())
            cardSetRepository.insertCardSets(cardSets.map { it.toDatabaseEntity() })
        }

        totalOffset += cardSetResponse.totalItems

        paginate(
            totalCount = productResponse.totalItems,
            paginationSize = DEFAULT_QUERY_LIMIT,
            paginate = { offset, paginationSize -> productRepository.fetchProducts(offset, paginationSize).getResponseOrThrow().results },
        ) { offset, products ->

            updateProgress(((totalOffset + offset.toDouble()) / totalItems * 100).toInt())

            productRepository.insertProducts(products.map { it.toDatabaseEntity() })
            products.forEach { cardItem -> cardItem.skus?.let {
                productRepository.insertSkus(cardItem.skus.map { it.toDatabaseEntity() })
            } }
        }
    }
}
