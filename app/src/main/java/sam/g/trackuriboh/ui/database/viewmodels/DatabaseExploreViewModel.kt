package sam.g.trackuriboh.ui.database.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.utils.DATABASE_ASSET_CREATION_DATE
import sam.g.trackuriboh.workers.DatabaseDownloadWorker
import sam.g.trackuriboh.workers.DatabaseUpdateCheckWorker
import sam.g.trackuriboh.workers.DatabaseUpdateWorker
import sam.g.trackuriboh.workers.PriceSyncWorker
import sam.g.trackuriboh.workers.WorkRequestManager
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DatabaseExploreViewModel @Inject constructor(
    workManager: WorkManager,
    private val workRequestManager: WorkRequestManager,
    private val sharedPreferences: SharedPreferences,
    private val cardSetRepository: CardSetRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    sealed class ButtonStatus {
        data class UpdateAvailable(val onUpdateButtonClick: () -> Unit) :
            ButtonStatus()

        object UpToDate : ButtonStatus()
    }

    data class UpdateButtonUiState(
        val enabled: Boolean,
        val status: ButtonStatus,
    )

    data class DatabaseStatusUiState(
        val lastUpdated: Date? = null,
        val totalCardSetCount: Int? = null,
        val totalCardCount: Int? = null,
    )

    val databaseUpdateButtonState: LiveData<UpdateButtonUiState>
        get() = _databaseUpdateButtonState

    val isUpdateRunning: LiveData<Boolean>
        get() = _isUpdateRunning

    private val databaseUpdateWorkInfoLiveData = Transformations.map(
        workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateWorker.workerName)
    ) {
        it.firstOrNull()
    }

    private val databaseUpdateCheckWorkInfoLiveData = Transformations.map(
        workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateCheckWorker.workerName)
    ) {
        it.firstOrNull()
    }

    private val databaseDownloadWorkInfoLiveData = Transformations.map(
        workManager.getWorkInfosForUniqueWorkLiveData(DatabaseDownloadWorker.workerName)
    ) {
        it.firstOrNull()
    }

    private val priceSyncWorkInfoLiveData =
        Transformations.map(workManager.getWorkInfosForUniqueWorkLiveData(PriceSyncWorker.workerName)) {
            it.firstOrNull()
        }

    private val forceRefresh = MediatorLiveData<Boolean>().apply {
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

        addSource(priceSyncWorkInfoLiveData) {
            if (it?.state == WorkInfo.State.SUCCEEDED) {
                value = true
            }
        }
    }

    val recentCardSetsWithProducts = forceRefresh.switchMap {
        liveData {
            emit(cardSetRepository.getRecentCardSetsAndMostExpensiveCards(5, 5))
        }
    }

    val databaseInfoUiState = forceRefresh.switchMap {
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

    private val _databaseUpdateButtonState = MediatorLiveData<UpdateButtonUiState>().apply {
        value = UpdateButtonUiState(enabled = true, status = ButtonStatus.UpToDate)

        addSource(databaseUpdateWorkInfoLiveData) {
            if (it?.state == WorkInfo.State.SUCCEEDED) {
                // Once the update is done, set the button to update to date and refresh
                value = value?.copy(status = ButtonStatus.UpToDate)
            }
        }

        addSource(databaseDownloadWorkInfoLiveData) {
            if (it?.state == WorkInfo.State.SUCCEEDED) {
                value = value?.copy(status = ButtonStatus.UpToDate)
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
                    val updateCardSetIds =
                        it.outputData.getLongArray(DatabaseUpdateCheckWorker.UPDATE_CARD_SET_IDS_RESULT)

                    value = UpdateButtonUiState(
                        enabled = true,
                        status = ButtonStatus.UpdateAvailable {
                            enqueueDatabaseUpdate(
                                updateCardSetIds
                            )
                        })
                }
            }
        }

        addSource(priceSyncWorkInfoLiveData) {
            if (it?.state == WorkInfo.State.RUNNING) {
                value = value?.copy(enabled = false)
            } else if (it?.state == WorkInfo.State.SUCCEEDED) {
                value = value?.copy(enabled = true)
            }
        }
    }

    private fun loadDatabaseStatusInfo() =
        MediatorLiveData<DatabaseStatusUiState>().apply {
            viewModelScope.launch {
                val lastUpdateDate = Date(
                    sharedPreferences.getLong(
                        DatabaseUpdateWorker.DATABASE_LAST_UPDATED_DATE_SHAREDPREF_KEY,
                        DATABASE_ASSET_CREATION_DATE
                    )
                )
                val totalCardSetCount = cardSetRepository.getTotalCardSetCount()
                val totalCardCount = productRepository.getCardCount()

                value = DatabaseStatusUiState(
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