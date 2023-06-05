package sam.g.trackuriboh.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.data.repository.SkuRepository

/**
 * Worker to sync prices in the background. It will try to fetch prices for all skus, but it's okay if it fails
 * since it runs silently in the background
 */
@HiltWorker
class PriceSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val productRepository: ProductRepository,
    private val skuRepository: SkuRepository,
    private val priceRepository: PriceRepository,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        val workerName: String = PriceSyncWorker::class.java.name
    }
    override suspend fun doWork(): Result {
        return try {
            firebaseAnalytics.logEvent(Events.PRICE_SYNC_WORKER_START, null)

            val skuCount = skuRepository.getSkuCount()

            paginate(
                totalCount = skuCount,
                paginationSize = GET_REQUEST_ID_QUERY_LIMIT
            ) { offset, paginationSize ->
                val skuIds = skuRepository.getSkuIdsPaginated(offset, paginationSize)

                priceRepository.updatePricesForSkus(skuIds)
            }

            val productCount = productRepository.getCardCount()

            paginate(
                totalCount = productCount,
                paginationSize = GET_REQUEST_ID_QUERY_LIMIT
            ) { offset, paginationSize ->
                val productIds =
                    productRepository.getProductIdsPaginated(offset, paginationSize)

                priceRepository.updatePricesForProducts(productIds)
            }

            firebaseAnalytics.logEvent(Events.PRICE_SYNC_SUCCESS, null)

            Result.success()
        } catch (e: Exception) {
            firebaseCrashlytics.recordException(e)
            Result.failure()
        }
    }
}