package sam.g.trackuriboh.ui_database.viewmodels

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.*
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.utils.SingleEvent
import sam.g.trackuriboh.utils.SnackbarType
import sam.g.trackuriboh.workers.DatabaseDownloadWorker
import sam.g.trackuriboh.workers.DatabaseUpdateCheckWorker
import sam.g.trackuriboh.workers.DatabaseUpdateWorker
import sam.g.trackuriboh.workers.DatabaseUpdateWorker.Companion.CARD_SET_IDS_INPUT_KEY
import sam.g.trackuriboh.workers.TAG_USER_TRIGGERED
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val productRepository: ProductRepository,
    private val application: Application,
) : ViewModel() {

    sealed class UiAction {
        data class Snackbar(val message: String, val snackbarType: SnackbarType) : UiAction()
        data class Loading(val progress: Int,  val indeterminate: Boolean, val message: String) : UiAction()
        data class UpdateAvailable(val cardSetIds: LongArray?) : UiAction() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as UpdateAvailable

                if (cardSetIds != null) {
                    if (other.cardSetIds == null) return false
                    if (!cardSetIds.contentEquals(other.cardSetIds)) return false
                } else if (other.cardSetIds != null) return false

                return true
            }

            override fun hashCode(): Int {
                return cardSetIds?.contentHashCode() ?: 0
            }
        }
    }

    val searchSuggestionsCursor: LiveData<Cursor>
        get() = _searchSuggestionsCursor
    private val _searchSuggestionsCursor = MutableLiveData<Cursor>()

    val action: LiveData<SingleEvent<UiAction>>
        get() = _action

    private val _action = MediatorLiveData<SingleEvent<UiAction>>()

    init {
        _action.addSource(workManager.getWorkInfosForUniqueWorkLiveData(DatabaseDownloadWorker.WORKER_TAG)) {
            handleWorkInfo(
                workInfoList = it,
                runningUiAction = { workInfo ->
                    UiAction.Loading(
                        workInfo.progress.getInt(DatabaseDownloadWorker.Progress, 0),
                        false,
                        application.getString(R.string.database_download_info)
                    )
                },
                succeededUiAction = { UiAction.Snackbar(application.getString(R.string.database_download_success), SnackbarType.SUCCESS) },
                failedUiAction = { UiAction.Snackbar(application.getString(R.string.database_download_failed), SnackbarType.ERROR) },
                cancelledUiAction = { UiAction.Snackbar(application.getString(R.string.database_download_cancelled), SnackbarType.INFO) },
            )
        }

        _action.addSource(workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateCheckWorker.WORKER_TAG)) {

            // If it's triggered by user, we should show all the feedback. Otherwise we should just asking to update
            // if there's an update available.
            handleWorkInfo(
                workInfoList = it,
                runningUiAction = { workInfo ->
                    if (workInfo.tags.contains(TAG_USER_TRIGGERED)) {
                        UiAction.Loading(
                            0, true, application.getString(R.string.database_update_check_info)
                        )
                    } else {
                        null
                    }
                },
                succeededUiAction = { workInfo ->
                    if (workInfo.outputData.getLongArray(DatabaseUpdateCheckWorker.UPDATE_CARD_SET_IDS_RESULT)?.isEmpty() == true) {
                        if (workInfo.tags.contains(TAG_USER_TRIGGERED)) {
                            UiAction.Snackbar(
                                application.getString(R.string.database_update_check_no_update_available),
                                SnackbarType.INFO
                            )
                        } else {
                            null
                        }
                    } else {
                        UiAction.UpdateAvailable(workInfo.outputData.getLongArray(DatabaseUpdateCheckWorker.UPDATE_CARD_SET_IDS_RESULT))
                    }
                },
                failedUiAction = { workInfo ->
                    if (workInfo.tags.contains(TAG_USER_TRIGGERED)) {
                        UiAction.Snackbar(application.getString(R.string.database_update_check_failed), SnackbarType.ERROR)
                    } else {
                        null
                    }
                },
                cancelledUiAction = { workInfo ->
                    if (workInfo.tags.contains(TAG_USER_TRIGGERED)) {
                        UiAction.Snackbar(application.getString(R.string.database_update_check_cancelled), SnackbarType.INFO)
                    } else {
                        null
                    }
                }
            )
        }

        _action.addSource(workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateWorker.WORKER_TAG)) {
            handleWorkInfo(
                workInfoList = it,
                runningUiAction = {
                    UiAction.Loading(
                        0, true, application.getString(R.string.database_update_info)
                    )
                },
                succeededUiAction = { UiAction.Snackbar(application.getString(R.string.database_update_success), SnackbarType.SUCCESS) },
                failedUiAction = { UiAction.Snackbar(application.getString(R.string.database_update_failed), SnackbarType.ERROR) },
                cancelledUiAction = { UiAction.Snackbar(application.getString(R.string.database_update_cancelled), SnackbarType.INFO) }
            )
        }
    }

    private fun handleWorkInfo(
        workInfoList: List<WorkInfo>,
        runningUiAction: (workInfo: WorkInfo) -> UiAction?,
        succeededUiAction: (workInfo: WorkInfo) -> UiAction?,
        failedUiAction: (workInfo: WorkInfo) -> UiAction?,
        cancelledUiAction: (workInfo: WorkInfo) -> UiAction?,
    ) {
        if (workInfoList.isEmpty()) {
            return
        }

        val workInfo = workInfoList.first()

        when {
            workInfo.state == WorkInfo.State.RUNNING -> runningUiAction(workInfo)?.let { _action.value = SingleEvent(it) }
            workInfo.state.isFinished -> {

                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> succeededUiAction(workInfo)?.let { _action.value = SingleEvent(it) }
                    WorkInfo.State.FAILED -> failedUiAction(workInfo)?.let { _action.value = SingleEvent(it) }
                    WorkInfo.State.CANCELLED -> cancelledUiAction(workInfo)?.let { _action.value = SingleEvent(it) }
                    else -> { }
                }

                workManager.pruneWork()
            }
        }
    }

    fun downloadDatabase() {
        val databaseDownloadRequest = OneTimeWorkRequestBuilder<DatabaseDownloadWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build()
            )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            DatabaseDownloadWorker.WORKER_TAG,
            ExistingWorkPolicy.REPLACE,
            databaseDownloadRequest
        )
    }

    fun updateDatabase(updateCardSetIds: LongArray?) {
        val databaseUpdateRequest = OneTimeWorkRequestBuilder<DatabaseUpdateWorker>()
            .setInputData(workDataOf(CARD_SET_IDS_INPUT_KEY to updateCardSetIds))
            .build()

        workManager.enqueueUniqueWork(
            DatabaseUpdateWorker.WORKER_TAG,
            ExistingWorkPolicy.REPLACE,
            databaseUpdateRequest
        )
    }

    fun checkForDatabaseUpdates() {
        val databaseUpdateRequest = OneTimeWorkRequestBuilder<DatabaseUpdateCheckWorker>()
            .addTag(TAG_USER_TRIGGERED)
            .build()

        workManager.enqueueUniqueWork(
            DatabaseUpdateCheckWorker.WORKER_TAG,
            ExistingWorkPolicy.REPLACE,
            databaseUpdateRequest
        )
    }

    fun getSuggestions(query: String?) {
        viewModelScope.launch {
            _searchSuggestionsCursor.value = productRepository.getSuggestionsCursor(query)
        }
    }
}
