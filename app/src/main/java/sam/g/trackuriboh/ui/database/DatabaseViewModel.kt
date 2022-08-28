package sam.g.trackuriboh.ui.database

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.ui.common.utils.UiState
import sam.g.trackuriboh.utils.SingleEvent
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val workManager: WorkManager,
): ViewModel() {
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
}