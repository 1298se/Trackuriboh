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

    private val forceRefreshTrigger = MutableLiveData<Boolean>().apply {
        value = false
    }

    val recentCardSetsWithProducts = forceRefreshTrigger.switchMap {
        liveData {
            emit(cardSetRepository.getRecentSetsWithCards(5, 10, it))
        }
    }

    val databaseInfoUiState = forceRefreshTrigger.switchMap {
        loadDatabaseStatusInfo()
    }

    private val databaseUpdateWorkInfoLiveData = Transformations.map(
        workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateWorker.workerName)) {
        it.firstOrNull()
    }

    private val databaseUpdateCheckWorkInfoLiveData = Transformations.map(
        workManager.getWorkInfosForUniqueWorkLiveData(DatabaseUpdateCheckWorker.workerName)) {
        it.firstOrNull()
    }

    val databaseUpdateButtonState = MediatorLiveData<DatabaseStatusView.ButtonState>().apply {
        value = DatabaseStatusView.ButtonState.UpToDate

        addSource(databaseUpdateWorkInfoLiveData) {
            when (it?.state) {
                WorkInfo.State.RUNNING -> {
                    value = DatabaseStatusView.ButtonState.Updating
                }
                WorkInfo.State.SUCCEEDED -> {
                    value = DatabaseStatusView.ButtonState.UpToDate

                    forceRefreshTrigger.value = true
                }
                WorkInfo.State.FAILED -> {}
                WorkInfo.State.BLOCKED -> {}
                WorkInfo.State.CANCELLED -> {}
                else -> {}
            }
        }

        addSource(databaseUpdateCheckWorkInfoLiveData) {
            if (databaseUpdateWorkInfoLiveData.value?.state != WorkInfo.State.RUNNING) {
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

                if (it?.state == WorkInfo.State.SUCCEEDED && lastUpdateCheckDate >= lastUpdateDate) {
                    val isUpdateAvailable = it.outputData.getBoolean(
                        DatabaseUpdateCheckWorker.UPDATE_AVAILABLE_RESULT,
                        false
                    )

                    if (isUpdateAvailable) {
                        val updateCardSetIds = it.outputData.getLongArray(DatabaseUpdateCheckWorker.UPDATE_CARD_SET_IDS_RESULT)

                        value = DatabaseStatusView.ButtonState.UpdateAvailable {
                            enqueueDatabaseUpdate(updateCardSetIds)
                        }
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