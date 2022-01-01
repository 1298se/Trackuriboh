package sam.g.trackuriboh.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
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
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val NETWORK_REQUEST_DELAY = 250L
    }
    override suspend fun doWork(): Result {
        return try {
            Log.d("WORKER", "PriceSyncWorker started")

            var offset = 0

            while (true) {
                val skuIds = skuRepository.getSkuIdsPaginated(offset, SKU_DEFAULT_QUERY_LIMIT)

                if (skuIds.isEmpty()) {
                    break
                }

                val resource = priceRepository.getPricesForSkuIds(skuIds)

                if (resource is Resource.Failure) {
                    throw IOException(resource.exception)
                }

                offset += SKU_DEFAULT_QUERY_LIMIT

                Log.d("OFFSET", offset.toString())

                delay(NETWORK_REQUEST_DELAY)
            }

            Log.d("WORKER", "PriceSyncWorker completed")

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}