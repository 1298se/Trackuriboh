package sam.g.trackuriboh.ui.database.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.ui.database.DatabaseStatusView
import sam.g.trackuriboh.utils.DATABASE_ASSET_CREATION_DATE
import sam.g.trackuriboh.workers.DatabaseDownloadWorker
import sam.g.trackuriboh.workers.DatabaseUpdateCheckWorker
import sam.g.trackuriboh.workers.DatabaseUpdateWorker
import sam.g.trackuriboh.workers.WorkRequestManager
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DatabaseExploreViewModel @Inject constructor(
    workManager: WorkManager,
    private val workRequestManager: WorkRequestManager,
    private val sharedPreferences: SharedPreferences,
    private val cardSetRepository: CardSetRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    val databaseUpdateButtonState: LiveData<DatabaseStatusView.ButtonState>
        get() = _databaseUpdateButtonState

    val isUpdateRunning: LiveData<Boolean>
        get() = _isUpdateRunning

    private val databaseUpdateWorkInfoLiveData = Transformations.map(
        workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateWorker.workerName)) {
        it.firstOrNull()
    }

    private val databaseUpdateCheckWorkInfoLiveData = Transformations.map(
        workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateCheckWorker.workerName)) {
        it.firstOrNull()
    }

    private val databaseDownloadWorkInfoLiveData = Transformations.map(
        workManager.getWorkInfosForUniqueWorkLiveData(DatabaseDownloadWorker.workerName)) {
        it.firstOrNull()
    }

    private val forceRefreshTrigger = MediatorLiveData<Boolean>().apply {
        value = true

        addSource(databaseUpdateWorkInfoLiveData) {
            if (it?.state == WorkInfo.State.SUCCEEDED) {
                value = true
            }
        }

        addSource(databaseDownloadWorkInfoLiveData) {
            if (it?.state == WorkInfo.State.SUCCEEDED) {
                value = true
            }
        }
    }

    val recentCardSetsWithProducts = forceRefreshTrigger.switchMap {
        liveData {
            emit(cardSetRepository.getRecentSetsWithCards(5, 10, it))
        }
    }

    val databaseInfoUiState = forceRefreshTrigger.switchMap {
        loadDatabaseStatusInfo()
    }

    private val _isUpdateRunning = MediatorLiveData<Boolean>().apply {
        value = false

        addSource(databaseUpdateWorkInfoLiveData) {
            value = when (it?.state) {
                WorkInfo.State.RUNNING -> {
                    true
                }
                else -> {
                    false
                }
            }
        }

        addSource(databaseDownloadWorkInfoLiveData) {
            value = when (it?.state) {
                WorkInfo.State.RUNNING -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private val _databaseUpdateButtonState = MediatorLiveData<DatabaseStatusView.ButtonState>().apply {
        value = DatabaseStatusView.ButtonState.UpToDate

        addSource(databaseUpdateWorkInfoLiveData) {
            if (it?.state == WorkInfo.State.SUCCEEDED) {
                // Once the update is done, set the button to update to date and refresh
                value = DatabaseStatusView.ButtonState.UpToDate
            }
        }

        addSource(databaseDownloadWorkInfoLiveData) {
            if (it?.state == WorkInfo.State.SUCCEEDED) {
                value = DatabaseStatusView.ButtonState.UpToDate
            }
        }

        addSource(databaseUpdateCheckWorkInfoLiveData) {
            if (it?.state == WorkInfo.State.SUCCEEDED) {
                val lastUpdateDate = Date(
                    sharedPreferences.getLong(
                        DatabaseUpdateWorker.DATABASE_LAST_UPDATED_DATE_SHAREDPREF_KEY,
                        DATABASE_ASSET_CREATION_DATE
                    )
                )
                val lastUpdateCheckDate = Date(
                    sharedPreferences.getLong(
                        DatabaseUpdateCheckWorker.DATABASE_LAST_UPDATED_CHECK_DATE_SHAREDPREF_KEY,
                        DATABASE_ASSET_CREATION_DATE
                    )
                )

                val isUpdateAvailable = it.outputData.getBoolean(
                    DatabaseUpdateCheckWorker.UPDATE_AVAILABLE_RESULT,
                    false
                )

                // Check the last update check date and last update date
                if (lastUpdateCheckDate >= lastUpdateDate && isUpdateAvailable) {
                    val updateCardSetIds = it.outputData.getLongArray(DatabaseUpdateCheckWorker.UPDATE_CARD_SET_IDS_RESULT)

                    value = DatabaseStatusView.ButtonState.UpdateAvailable {
                        enqueueDatabaseUpdate(updateCardSetIds)
                    }
                }
            }
        }
    }

    private fun loadDatabaseStatusInfo() =
        MediatorLiveData<DatabaseStatusView.UiState>().apply {
            viewModelScope.launch {
                val lastUpdateDate = Date(sharedPreferences.getLong(DatabaseUpdateWorker.DATABASE_LAST_UPDATED_DATE_SHAREDPREF_KEY, DATABASE_ASSET_CREATION_DATE))
                val totalCardSetCount = cardSetRepository.getTotalCardSetCount()
                val totalCardCount = productRepository.getTotalCardCount()

                value = DatabaseStatusView.UiState(
                    lastUpdated = lastUpdateDate,
                    totalCardSetCount = totalCardSetCount,
                    totalCardCount = totalCardCount,
                )
            }
        }

    private fun enqueueDatabaseUpdate(updateCardSetIds: LongArray?) {
        workRequestManager.enqueueDatabaseUpdateWorker(updateCardSetIds)
    }
}