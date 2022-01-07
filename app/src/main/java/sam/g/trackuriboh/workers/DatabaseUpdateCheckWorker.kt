package sam.g.trackuriboh.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.os.bundleOf
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.network.responses.CardSetResponse
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.CatalogRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.di.NetworkModule
import sam.g.trackuriboh.utils.DATABASE_ASSET_CREATION_DATE
import sam.g.trackuriboh.workers.DatabaseUpdateWorker.Companion.DATABASE_LAST_UPDATED_DATE
import java.util.*

@HiltWorker
class DatabaseUpdateCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cardSetRepository: CardSetRepository,
    private val productRepository: ProductRepository,
    private val catalogRepository: CatalogRepository,
    private val sharedPreferences: SharedPreferences,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val USER_TRIGGERED_WORKER_NAME = "DatabaseUpdateCheckWorker_UserTriggered"
        const val BACKGROUND_WORKER_NAME = "DatabaseUpdateCheckWorker_Background"

        const val UPDATE_CARD_SET_IDS_RESULT = "DatabaseUpdateCheckWorker_CardSetIdsResult"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.Default){
        try {
            firebaseAnalytics.logEvent(Events.UPDATE_CHECK_WORKER_START, null)

            with(catalogRepository) {
                val cardRarityResponse = fetchCardRarities().getResponseOrThrow()
                val printingResponse = fetchProductPrintings().getResponseOrThrow()
                val conditionResponse = fetchProductConditions().getResponseOrThrow()

                insertCardRarities(cardRarityResponse.results.map { it.toDatabaseEntity() })
                insertPrintings(printingResponse.results.map { it.toDatabaseEntity() })
                insertConditions(conditionResponse.results.map { it.toDatabaseEntity() })
            }

            val fetchedCardSetCount = cardSetRepository.fetchCardSets(limit = 1).getResponseOrThrow().totalItems

            val fetchedCardSets = mutableListOf<CardSetResponse.CardSetItem>()

            paginate(
                totalCount = fetchedCardSetCount,
                paginationSize = NetworkModule.DEFAULT_QUERY_LIMIT,
                paginate = { offset, paginationSize -> cardSetRepository.fetchCardSets(offset, paginationSize).getResponseOrThrow().results }
            ) { _, list ->
                fetchedCardSets.addAll(list)
            }


            val currentCardSetsWithCounts = cardSetRepository.getCardSetsWithCount()
            val currentCardSetIds = currentCardSetsWithCounts.map { it.key.id }.toSet()

            // Get the diff between the two lists
            val diffCardSet = fetchedCardSets.filter { it.id !in currentCardSetIds }

            // We need to fetch the products from both the diff sets and unreleased sets as they may
            // have been updated
            val lastUpdatedDate = Date(sharedPreferences.getLong(DATABASE_LAST_UPDATED_DATE, DATABASE_ASSET_CREATION_DATE))
            val unreleasedCardSets = currentCardSetsWithCounts.filter { entry ->
                val cardSet = entry.key
                val count = entry.value

                cardSet.releaseDate?.let {
                    // If the set is unreleased yet, we check if the count from the API is different than the current count in
                    // database. If so, we should add it to the update list.
                    if (it >= lastUpdatedDate) {
                        val updatedCount = productRepository.fetchProducts(limit = 1, cardSetId = cardSet.id).getResponseOrThrow().totalItems

                        updatedCount != count
                    } else {
                        false
                    }
                } ?: false

            }
            val updateCardSetIds = diffCardSet.map { it.id }.toMutableSet().apply {
                addAll(unreleasedCardSets.keys.map { it.id })
            }.toLongArray()

            firebaseAnalytics.logEvent(Events.UPDATE_CHECK_WORKER_SUCCESS, bundleOf(
                "updateCardSetIds" to updateCardSetIds
            ))

            Result.success(workDataOf(UPDATE_CARD_SET_IDS_RESULT to updateCardSetIds))
        } catch (e: Exception) {
            firebaseCrashlytics.recordException(e)
            Result.failure()
        }
    }
}