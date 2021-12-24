package sam.g.trackuriboh.ui_database.viewmodels

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.*
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.ui_common.UiState
import sam.g.trackuriboh.utils.SingleEvent
import sam.g.trackuriboh.workers.DatabaseDownloadWorker
import sam.g.trackuriboh.workers.DatabaseUpdateCheckWorker
import sam.g.trackuriboh.workers.DatabaseUpdateWorker
import sam.g.trackuriboh.workers.WorkRequestManager
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val workRequestManager: WorkRequestManager,
    private val workManager: WorkManager,
    private val productRepository: ProductRepository,
    private val cardSetRepository: CardSetRepository,
    private val application: Application,
) : ViewModel() {

    enum class Page(val position: Int) {
        CARDS(0),
        CARD_SETS(1),
    }

    val searchSuggestionsCursor: LiveData<Cursor>
        get() = _searchSuggestionsCursor

    private val _searchSuggestionsCursor = MediatorLiveData<Cursor>()

    val databaseDownloadState: LiveData<SingleEvent<UiState<WorkInfo>>>
        get() = _databaseDownloadState

    val databaseUpdateCheckState: LiveData<SingleEvent<UiState<WorkInfo>>>
        get() = _databaseUpdateCheckState

    val databaseUpdateState: LiveData<SingleEvent<UiState<WorkInfo>>>
        get() = _databaseUpdateState

    private val _databaseDownloadState = MediatorLiveData<SingleEvent<UiState<WorkInfo>>>()
    private val _databaseUpdateCheckState = MediatorLiveData<SingleEvent<UiState<WorkInfo>>>()
    private val _databaseUpdateState = MediatorLiveData<SingleEvent<UiState<WorkInfo>>>()

    private val currentPage = MutableLiveData<Page>()

    private var suggestionsQuery = MutableLiveData<String?>(null)

    init {
        _searchSuggestionsCursor.addSource(suggestionsQuery) {
            getSearchSuggestions(it)
        }

        _searchSuggestionsCursor.addSource(currentPage) {
            getSearchSuggestions(suggestionsQuery.value)
        }


        // Observating database downloads
        _databaseDownloadState.addSource(
            workManager.getWorkInfosForUniqueWorkLiveData(DatabaseDownloadWorker::class.java.name)
        ) {
            onWorkInfoChanged(
                workInfoList = it,
                observable = _databaseDownloadState,
                runningUiState = { workInfo ->
                    UiState.Loading(
                        message = application.getString(R.string.database_download_info),
                        data = workInfo
                    )
                },
                succeededUiState = { workInfo ->
                    UiState.Success(
                        data = workInfo,
                        message = application.getString(R.string.database_download_success)
                    )
                },
                failedUiState = { workInfo ->
                    UiState.Failure(
                        data = workInfo,
                        message = application.getString(R.string.database_download_failed),
                    )
                },
                cancelledUiState = { workInfo ->
                    UiState.Failure(
                        data = workInfo,
                        message = application.getString(R.string.database_download_cancelled)
                    )
                },
            )
        }

        // Observating database updates checks that are user triggered
        _databaseUpdateCheckState.addSource(
            workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateCheckWorker.USER_TRIGGERED_WORKER_NAME)
        ) {
            onWorkInfoChanged(
                workInfoList = it,
                observable = _databaseUpdateCheckState,
                runningUiState = { workInfo ->
                    UiState.Loading(
                        message = application.getString(R.string.database_update_check_info),
                        data = workInfo
                    )
                },
                succeededUiState = { workInfo ->
                    if (workInfo.outputData.getLongArray(DatabaseUpdateCheckWorker.UPDATE_CARD_SET_IDS_RESULT)?.size != 0) {
                        UiState.Success(
                            data = workInfo,
                            message = application.getString(R.string.database_update_check_update_available_message)
                        )
                    } else {
                        UiState.Success(
                            data = null,
                            message = application.getString(R.string.database_update_check_no_update_available),
                        )
                    }
                },
                failedUiState = { workInfo ->
                    UiState.Failure(
                        data = workInfo,
                        message = application.getString(R.string.database_update_check_failed),
                    )
                },
                cancelledUiState = { workInfo ->
                    UiState.Failure(
                        data = workInfo,
                        message = application.getString(R.string.database_update_check_cancelled)
                    )
                },
            )
        }

        // Observing database updates checks that are background worker triggered
        _databaseUpdateState.addSource(
            workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateCheckWorker.BACKGROUND_WORKER_NAME)
        ) {
            onWorkInfoChanged(
                workInfoList = it,
                observable = _databaseUpdateCheckState,
                succeededUiState = { workInfo ->
                    if (workInfo.outputData.getLongArray(DatabaseUpdateCheckWorker.UPDATE_CARD_SET_IDS_RESULT)?.size != 0) {
                        UiState.Success(
                            data = workInfo,
                            message = application.getString(R.string.database_update_check_update_available_message)
                        )
                    } else {
                        null
                    }
                },
            )
        }

        // Observing database updates
        _databaseUpdateState.addSource(
            workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateWorker::class.java.name)
        ) {
            onWorkInfoChanged(
                workInfoList = it,
                observable = _databaseUpdateState,
                runningUiState = { workInfo ->
                    UiState.Loading(
                        message = application.getString(R.string.database_update_info),
                        data = workInfo
                    )
                },
                succeededUiState = { workInfo ->
                    UiState.Success(
                        data = workInfo,
                        message = application.getString(R.string.database_update_success),
                    )
                },
                failedUiState = { workInfo ->
                    UiState.Failure(
                        data = workInfo,
                        message = application.getString(R.string.database_update_failed),
                    )
                },
                cancelledUiState = { workInfo ->
                    UiState.Failure(
                        data = workInfo,
                        message = application.getString(R.string.database_update_cancelled)
                    )
                },
            )
        }
    }

    fun downloadDatabase() {
        workRequestManager.enqueueDatabaseDownloadWorker()
    }

    fun updateDatabase(updateCardSetIds: LongArray?) {
        workRequestManager.enqueueDatabaseUpdateWorker(updateCardSetIds)
    }

    fun checkForDatabaseUpdates() {
        workRequestManager.enqueueDatabaseUpdateCheck(true)
    }

    fun setSearchSuggestion(query: String?) {
        suggestionsQuery.value = query
    }

    fun setCurrentPage(page: Page) {
        currentPage.value = page
    }

    private fun onWorkInfoChanged(
        workInfoList: List<WorkInfo>,
        observable: MutableLiveData<SingleEvent<UiState<WorkInfo>>>,
        runningUiState: (workInfo: WorkInfo) -> UiState<WorkInfo>? = { null },
        succeededUiState: (workInfo: WorkInfo) -> UiState<WorkInfo>? = { null },
        failedUiState: (workInfo: WorkInfo) -> UiState<WorkInfo>? = { null },
        cancelledUiState: (workInfo: WorkInfo) -> UiState<WorkInfo>? = { null },
    ) {
        if (workInfoList.isEmpty()) {
            return
        }
        val workInfo = workInfoList.first()

        when (workInfo.state) {
            WorkInfo.State.RUNNING -> {
                runningUiState(workInfo)?.let { observable.value = SingleEvent(it) }
            }
            else -> {
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        succeededUiState(workInfo)?.let { observable.value = SingleEvent(it) }
                    }
                    WorkInfo.State.FAILED -> {
                        failedUiState(workInfo)?.let { observable.value = SingleEvent(it) }
                    }
                    WorkInfo.State.CANCELLED -> {
                        cancelledUiState(workInfo)?.let { observable.value = SingleEvent(it) }
                    }
                    else -> return
                }

                workManager.pruneWork()
            }
        }
    }

    private fun getSearchSuggestions(query: String?) {
        viewModelScope.launch {

            _searchSuggestionsCursor.value = when (currentPage.value) {
                Page.CARDS -> productRepository.getSuggestionsCursor(query)
                Page.CARD_SETS -> cardSetRepository.getSuggestionsCursor(query)
                else -> return@launch
            }
        }
    }
}
