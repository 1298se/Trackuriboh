package sam.g.trackuriboh.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import sam.g.trackuriboh.R
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.network.ResponseToDatabaseEntityConverter
import sam.g.trackuriboh.data.repository.*
import sam.g.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT
import sam.g.trackuriboh.utils.DB_SYNC_PROGRESS_NOTIFICATION_ID
import sam.g.trackuriboh.utils.DB_SYNC_STATE_NOTIFICATION_ID
import sam.g.trackuriboh.utils.MAX_PROGRESS
import sam.g.trackuriboh.utils.createNotificationBuilder
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
    private val skuRepository: SkuRepository,
    private val userListRepository: UserListRepository,
    private val appDatabase: AppDatabase,
    private val sharedPreferences: SharedPreferences,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val workRequestManager: WorkRequestManager,
    private val responseConverter: ResponseToDatabaseEntityConverter,
) : CoroutineWorker(appContext, workerParams) {

    private val progressNotificationBuilder by lazy {
        createNotificationBuilder(
            context = applicationContext,
            notificationTitle = applicationContext.getString(R.string.database_download_worker_notification_title),
            message = applicationContext.getString(R.string.database_download_worker_notification_message),
            channelId = applicationContext.getString(R.string.database_sync_notification_channel_id),
            channelName = applicationContext.getString(R.string.database_sync_notification_channel_name),
            showProgress = true,
            ongoing = true,
            autoCancel = false
        )
    }

    private val stateNotificationBuilder by lazy {
        createNotificationBuilder(
            context = applicationContext,
            notificationTitle = applicationContext.getString(R.string.database_download_worker_notification_title),
            message = null,
            channelId = applicationContext.getString(R.string.database_sync_notification_channel_id),
            channelName = applicationContext.getString(R.string.database_sync_notification_channel_name),
        )
    }

    override suspend fun doWork(): Result  = withContext(Dispatchers.Default){
        try {

            firebaseAnalytics.logEvent(Events.DATABASE_DOWNLOAD_WORKER_START, null)

            updateProgress(0)

            // Save the user's current lists
            val userListEntries = userListRepository.getAllUserListEntries()
            val userLists = userListRepository.getAllUserLists()

            appDatabase.clearCardDatabaseTables()



            with(catalogRepository) {
                val cardRarityResponse = fetchCardRarities().getResponseOrThrow()
                val printingResponse = fetchProductPrintings().getResponseOrThrow()
                val conditionResponse = fetchProductConditions().getResponseOrThrow()

                insertCardRarities(cardRarityResponse.results.map { responseConverter.toCardRarity(it) })
                insertPrintings(printingResponse.results.map { responseConverter.toPrinting(it) })
                insertConditions(conditionResponse.results.map { responseConverter.toCondition(it) })
            }

            downloadDatabase()

            with(sharedPreferences.edit()) {
                putLong(DatabaseUpdateWorker.DATABASE_LAST_UPDATED_DATE_SHAREDPREF_KEY, Date().time)
                commit()
            }

            with(NotificationManagerCompat.from(applicationContext)) {
                stateNotificationBuilder.setContentText(applicationContext.getString(R.string.database_download_success))
                notify(DB_SYNC_STATE_NOTIFICATION_ID, stateNotificationBuilder.build())
            }

            // insert the user lists back
            userListRepository.insertUserLists(userLists)
            userListRepository.insertUserListEntries(userListEntries)

            // Enqueue price sync
            workRequestManager.enqueueOneTimePriceSync()

            firebaseAnalytics.logEvent(Events.DATABASE_DOWNLOAD_SUCCESS, null)

            Result.success()
        } catch (e: Exception) {
            firebaseCrashlytics.recordException(e)

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
                    if (e.cause is CancellationException)
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

        setProgress(workDataOf(WORKER_PROGRESS_KEY to progress))
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
            cardSetRepository.insertCardSets(cardSets.map { responseConverter.toCardSet(it) })
        }

        totalOffset += cardSetResponse.totalItems

        paginate(
            totalCount = productResponse.totalItems,
            paginationSize = DEFAULT_QUERY_LIMIT,
            paginate = { offset, paginationSize -> productRepository.fetchProducts(offset, paginationSize).getResponseOrThrow().results },
        ) { offset, products ->

            updateProgress(((totalOffset + offset.toDouble()) / totalItems * 100).toInt())

            productRepository.insertProducts(products.map { responseConverter.toCardProduct(it) })
            products.forEach { cardItem -> cardItem.skus?.let {
                skuRepository.insertSkus(cardItem.skus.map { responseConverter.toSku(it) })
            } }
        }
    }
}
