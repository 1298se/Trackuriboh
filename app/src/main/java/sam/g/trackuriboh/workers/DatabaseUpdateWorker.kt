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
import sam.g.trackuriboh.data.network.ResponseToDatabaseEntityConverter
import sam.g.trackuriboh.data.network.responses.CardResponse
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.CatalogRepository
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.data.repository.SkuRepository
import sam.g.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT
import java.util.Date
import kotlin.math.min

@HiltWorker
class DatabaseUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cardSetRepository: CardSetRepository,
    private val catalogRepository: CatalogRepository,
    private val productRepository: ProductRepository,
    private val skuRepository: SkuRepository,
    private val priceRepository: PriceRepository,
    private val sharedPreferences: SharedPreferences,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val responseConverter: ResponseToDatabaseEntityConverter,
    private val workRequestManager: WorkRequestManager,
) : CoroutineWorker(appContext, workerParams) {
    companion object {

        const val DATABASE_LAST_UPDATED_DATE_KEY = "DatabaseCheckWorker_LastUpdatedDate"

        const val CARD_SET_IDS_INPUT_KEY = "DatabaseUpdateWorker_CardSetIdsInputKey"

        val workerName: String = DatabaseUpdateWorker::class.java.name
    }

    override suspend fun doWork(): Result  = withContext(Dispatchers.Default) {
        try {
            firebaseAnalytics.logEvent(Events.UPDATE_WORKER_START, null)

            with(catalogRepository) {
                val printingResponse = fetchProductPrintings().getResponseOrThrow()
                val conditionResponse = fetchProductConditions().getResponseOrThrow()
                val cardRarityResponse = fetchCardRarities().getResponseOrThrow()

                upsertPrintings(printingResponse.results.map { responseConverter.toPrinting(it) })
                upsertConditions(conditionResponse.results.map { responseConverter.toCondition(it) })
                upsertCardRarities(cardRarityResponse.results.map { responseConverter.toCardRarity(it) })
            }

            val updateCardSetIds = inputData.getLongArray(CARD_SET_IDS_INPUT_KEY)?.toList() ?: return@withContext Result.success()

            // Fetch and update the card sets in batches of GET_REQUEST_ID_QUERY_LIMIT because the URL can be too long
            // resulting in 404
            paginate(
                totalCount = updateCardSetIds.size,
                paginationSize = GET_REQUEST_ID_QUERY_LIMIT
            ) { offset, limit ->
                val responses = cardSetRepository.fetchCardSetDetails(
                    updateCardSetIds.subList(
                        offset,
                        min(updateCardSetIds.size, offset + limit)
                    )
                ).getResponseOrThrow().results

                cardSetRepository.upsertCardSets(responses.map { responseConverter.toCardSet(it) })
            }

            // We "paginate" the updateCardSetIds since we only want 15 network requests in a batch.
            paginate(
                totalCount = updateCardSetIds.size,
                paginationSize = 1
            ) { cardSetOffset, _ ->
                val productList = mutableListOf<CardResponse.CardItem>()

                val cardSetId = updateCardSetIds[cardSetOffset]
                val productCount = productRepository.fetchProducts(
                    limit = 1,
                    cardSetId = cardSetId
                ).getResponseOrThrow().totalItems

                paginate(
                    totalCount = productCount,
                    paginationSize = DEFAULT_QUERY_LIMIT
                ) { offset, paginationSize ->
                    run {
                        val responses =
                            productRepository.fetchProducts(
                                offset,
                                paginationSize,
                                cardSetId
                            )
                                .getResponseOrThrow().results

                        val products = responses.map { responseConverter.toCardProduct(it) }
                        val skus = responses.filter { it.skus != null }
                            .flatMap { cardItem -> cardItem.skus!! }

                        productRepository.upsertProducts(products)
                        skuRepository.upsertSkus(skus.map { responseConverter.toSku(it) })
                        priceRepository.updatePricesForProducts(products.map { it.id })
                    }
                }
            }

            with(sharedPreferences.edit()) {
                putLong(DATABASE_LAST_UPDATED_DATE_KEY, Date().time)
                commit()
            }

            firebaseAnalytics.logEvent(
                Events.UPDATE_WORKER_SUCCESS, bundleOf(
                    "updateCardSetIds" to updateCardSetIds,
                )
            )

            // After the update, enqueue a price sync
            workRequestManager.enqueueOneTimePriceSync()

            Result.success()
        } catch (e: Exception) {
            firebaseCrashlytics.recordException(e)
            Result.failure(workDataOf("error message" to e.message))
        }
    }
}