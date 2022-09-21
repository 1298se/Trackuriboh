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
import sam.g.trackuriboh.data.network.responses.CardSetResponse
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.di.NetworkModule
import sam.g.trackuriboh.utils.DATABASE_ASSET_CREATION_DATE
import sam.g.trackuriboh.workers.DatabaseUpdateWorker.Companion.DATABASE_LAST_UPDATED_DATE_SHAREDPREF_KEY
import java.util.*

@HiltWorker
class DatabaseUpdateCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cardSetRepository: CardSetRepository,
    private val sharedPreferences: SharedPreferences,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val responseToDatabaseEntityConverter: ResponseToDatabaseEntityConverter
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

            val updateCardSets = mutableListOf<CardSetResponse.CardSetItem>()

            // We need to fetch the products from both the diff sets and unreleased sets as they may
            // have been updated
            val lastUpdatedDate = Date(sharedPreferences.getLong(DATABASE_LAST_UPDATED_DATE_SHAREDPREF_KEY, DATABASE_ASSET_CREATION_DATE))

            val existingSetModelsWithCountMap = cardSetRepository.getCardSetsWithCount()

            paginate(
                totalCount = fetchedCardSetCount,
                paginationSize = NetworkModule.DEFAULT_QUERY_LIMIT,
                paginate = { offset, paginationSize -> cardSetRepository.fetchCardSets(offset, paginationSize).getResponseOrThrow().results }
            ) { _, list ->
                for (item in list) {
                    val responseSetModel = responseToDatabaseEntityConverter.toCardSet(item)
                    val existingSetModel = cardSetRepository.getCardSet(item.id)

                    // If we don't have it in db, or it hasn't been released yet, or the modified
                    // date is after our db's modified date, then add to update list.
                    if (existingSetModel == null ||
                        responseSetModel.releaseDate?.after(lastUpdatedDate) == true ||
                        responseSetModel.modifiedDate?.after(existingSetModel.modifiedDate) == true)

                    updateCardSets.add(item)
                }
            }


            val updateCardSetIds = updateCardSets.map { it.id }.toLongArray()

            firebaseAnalytics.logEvent(Events.UPDATE_CHECK_WORKER_SUCCESS, bundleOf(
                "updateCardSetIds" to updateCardSetIds
            ))

            with(sharedPreferences.edit()) {
                putLong(DATABASE_LAST_UPDATED_CHECK_DATE_SHAREDPREF_KEY, Date().time)
                commit()
            }

            Result.success(workDataOf(
                UPDATE_AVAILABLE_RESULT to updateCardSetIds.isNotEmpty(),
                UPDATE_CARD_SET_IDS_RESULT to updateCardSetIds,
            ))
        } catch (e: Exception) {
            firebaseCrashlytics.recordException(e)
            Result.failure()
        }
    }
}