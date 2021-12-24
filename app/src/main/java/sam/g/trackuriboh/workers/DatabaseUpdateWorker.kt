package sam.g.trackuriboh.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import sam.g.trackuriboh.data.network.responses.CardResponse
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT
import java.util.*

@HiltWorker
class DatabaseUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cardSetRepository: CardSetRepository,
    private val productRepository: ProductRepository,
    private val sharedPreferences: SharedPreferences,
) : CoroutineWorker(appContext, workerParams) {
    companion object {

        const val DATABASE_LAST_UPDATED_DATE = "DatabaseUpdateCheckWorker_LastUpdatedDate"

        const val CARD_SET_IDS_INPUT_KEY = "DatabaseUpdateWorker_CardSetIdsInputKey"
    }

    override suspend fun doWork(): Result  = withContext(Dispatchers.Default) {
        try {
            val updateCardSetIds = inputData.getLongArray(CARD_SET_IDS_INPUT_KEY) ?: return@withContext Result.success()

            val updateCardSets = cardSetRepository.fetchCardSetDetails(updateCardSetIds.toList()).getResponseOrThrow().results

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
                }
            ) { _, list ->

                productRepository.insertProducts(list.map { it.toDatabaseEntity() })
                list.forEach { cardItem -> cardItem.skus?.let {
                    productRepository.insertSkus(cardItem.skus.map { it.toDatabaseEntity() })
                } }
            }

            with(sharedPreferences.edit()) {
                putLong(DATABASE_LAST_UPDATED_DATE, Date().time)
                commit()
            }

            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }
}