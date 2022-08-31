package sam.g.trackuriboh.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.os.bundleOf
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.network.responses.CardResponse
import sam.g.trackuriboh.data.network.responses.CardSetResponse
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.data.repository.SkuRepository
import sam.g.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT
import java.util.*

@HiltWorker
class DatabaseUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cardSetRepository: CardSetRepository,
    private val productRepository: ProductRepository,
    private val skuRepository: SkuRepository,
    private val sharedPreferences: SharedPreferences,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
) : CoroutineWorker(appContext, workerParams) {
    companion object {

        const val DATABASE_LAST_UPDATED_DATE_SHAREDPREF_KEY = "DatabaseCheckWorker_LastUpdatedDate"

        const val CARD_SET_IDS_INPUT_KEY = "DatabaseUpdateWorker_CardSetIdsInputKey"

        val workerName: String = DatabaseUpdateWorker::class.java.name
    }

    override suspend fun doWork(): Result  = withContext(Dispatchers.Default) {
        try {
            firebaseAnalytics.logEvent(Events.UPDATE_WORKER_START, null)

            val updateCardSetIds = inputData.getLongArray(CARD_SET_IDS_INPUT_KEY)?.toList() ?: return@withContext Result.success()

            val updateCardSets = mutableListOf<CardSetResponse.CardSetItem>()

            // Fetch and update the card sets in batches of 250 because the URL can be too long
            // resulting in 404
            paginate(
                totalCount = updateCardSetIds.size,
                paginationSize = GET_REQUEST_ID_QUERY_LIMIT,
                maxParallelRequests = 1,
                paginate = { cardSetOffset, limit ->
                    cardSetRepository.fetchCardSetDetails(
                        updateCardSetIds.subList(cardSetOffset, cardSetOffset + limit)
                    ).getResponseOrThrow().results
                },
                onPaginate = { _, list ->
                    cardSetRepository.insertCardSets(list.map { it.toDatabaseEntity() })
                }
            )

            cardSetRepository.insertCardSets(updateCardSets.map { it.toDatabaseEntity() })

            // We "paginate" the updateCardSetIds since we only want 15 network requests in a batch.
            // Pagination size is 1 because we need to load for every card set.
            paginate(
                totalCount = updateCardSetIds.size,
                paginationSize = 1,
                paginate = { cardSetOffset, _ ->
                    val productList = mutableListOf<CardResponse.CardItem>()

                    val cardSetId = updateCardSetIds[cardSetOffset]
                    val cardSetCount = productRepository.fetchProducts(
                        limit = 1,
                        cardSetId = cardSetId
                    ).getResponseOrThrow().totalItems

                    paginate(
                        totalCount = cardSetCount,
                        paginationSize = DEFAULT_QUERY_LIMIT,
                        paginate = { offset, paginationSize ->
                            productRepository.fetchProducts(offset, paginationSize, cardSetId).getResponseOrThrow().results }
                    ) { _, list ->
                        productList.addAll(list)
                    }

                    productList
                },
                onPaginate = { _, list ->
                    productRepository.insertProducts(list.map { it.toDatabaseEntity() })
                    list.forEach { cardItem -> cardItem.skus?.let {
                        skuRepository.insertSkus(cardItem.skus.map { it.toDatabaseEntity() })
                    } }
                }
            )
            with(sharedPreferences.edit()) {
                putLong(DATABASE_LAST_UPDATED_DATE_SHAREDPREF_KEY, Date().time)
                commit()
            }

            firebaseAnalytics.logEvent(Events.UPDATE_WORKER_SUCCESS, bundleOf(
                "updateCardSetIds" to updateCardSetIds,
            ))

            Result.success()
        } catch (e: Exception) {
            firebaseCrashlytics.recordException(e)
            Result.failure()
        }
    }
}