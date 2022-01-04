package sam.g.trackuriboh.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.SkuRepository
import sam.g.trackuriboh.data.repository.SkuRepository.Companion.SKU_DEFAULT_QUERY_LIMIT
import java.io.IOException

/**
 * Worker to sync prices in the background. It will try to fetch prices for all skus, but it's okay if it fails
 * since it runs silently in the background
 */
@HiltWorker
class PriceSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val skuRepository: SkuRepository,
    private val priceRepository: PriceRepository,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val NETWORK_REQUEST_DELAY = 250L
    }
    override suspend fun doWork(): Result {
        return try {
            firebaseAnalytics.logEvent(Events.PRICE_SYNC_WORKER_START, null)

            var offset = 0

            while (true) {
                val skuIds = skuRepository.getSkuIdsPaginated(offset, SKU_DEFAULT_QUERY_LIMIT)

                if (skuIds.isEmpty()) {
                    break
                }

                val resource = priceRepository.updatePricesForSkus(skuIds)

                if (resource is Resource.Failure) {
                    throw IOException(resource.exception)
                }

                offset += SKU_DEFAULT_QUERY_LIMIT

                delay(NETWORK_REQUEST_DELAY)
            }

            firebaseAnalytics.logEvent(Events.PRICE_SYNC_SUCCESS, null)

            Result.success()
        } catch (e: Exception) {
            firebaseCrashlytics.recordException(e)
            Result.failure()
        }
    }
}