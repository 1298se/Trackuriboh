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
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.di.NetworkModule
import java.util.Date

@HiltWorker
class DatabaseUpdateCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cardSetRepository: CardSetRepository,
    private val sharedPreferences: SharedPreferences,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val responseToDatabaseEntityConverter: ResponseToDatabaseEntityConverter,
    private val workRequestManager: WorkRequestManager,
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val DATABASE_LAST_UPDATED_CHECK_DATE_SHAREDPREF_KEY = "DatabaseUpdateCheckWorker_LastCheckDate"

        const val UPDATE_AVAILABLE_RESULT = "DatabaseUpdateCheckWorker_UpdateAvailableResult"
        const val UPDATE_CARD_SET_IDS_RESULT = "DatabaseUpdateCheckWorker_CardSetIdsResult"

        val workerName: String = DatabaseUpdateCheckWorker::class.java.name
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.Default){
        try {
            firebaseAnalytics.logEvent(Events.UPDATE_CHECK_WORKER_START, null)

            val fetchedCardSetCount = cardSetRepository.fetchCardSets(limit = 1).getResponseOrThrow().totalItems

            val updateCardSetIds = mutableListOf<Long?>()

            paginate(
                totalCount = fetchedCardSetCount,
                paginationSize = NetworkModule.DEFAULT_QUERY_LIMIT
            ) { offset, paginationSize ->
                cardSetRepository.fetchCardSets(offset, paginationSize).getResponseOrThrow().results
            }


            firebaseAnalytics.logEvent(
                Events.UPDATE_CHECK_WORKER_SUCCESS, bundleOf(
                    "updateCardSetIds" to updateCardSetIds
                )
            )

            with(sharedPreferences.edit()) {
                putLong(DATABASE_LAST_UPDATED_CHECK_DATE_SHAREDPREF_KEY, Date().time)
                commit()
            }

            val updateAvailable = updateCardSetIds.isNotEmpty()

            // If there's no update available, we can run a price sync
            if (!updateAvailable) {
                workRequestManager.enqueueOneTimePriceSync()
            }

            Result.success(
                workDataOf(
                    UPDATE_AVAILABLE_RESULT to updateAvailable,
                    UPDATE_CARD_SET_IDS_RESULT to updateCardSetIds.filterNotNull().toTypedArray(),
                )
            )
        } catch (e: Exception) {
            firebaseCrashlytics.recordException(e)
            e.printStackTrace()
            Result.failure()
        }
    }
}