package sam.g.trackuriboh.ui_database.viewmodels

import androidx.lifecycle.*
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.SingleEvent
import sam.g.trackuriboh.workers.DatabaseSyncWorker
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val workManager: WorkManager,
) : ViewModel() {

    val databaseSyncState: LiveData<SingleEvent<WorkInfo?>>
        get() = _databaseSyncState
    private val _databaseSyncState =  MediatorLiveData<SingleEvent<WorkInfo?>>()

    init {
        _databaseSyncState.addSource(workManager.getWorkInfosForUniqueWorkLiveData(DatabaseSyncWorker.WORKER_TAG)) {
            if (it.isEmpty()) {
                return@addSource
            }

            val workInfo = it.first()
            _databaseSyncState.value = SingleEvent(workInfo)

            // If it's done, prune it
            if (workInfo.state.isFinished) {
                workManager.pruneWork()
            }
        }
    }

    fun syncDatabase() {
        val databaseSyncWorkRequest = OneTimeWorkRequestBuilder<DatabaseSyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            DatabaseSyncWorker.WORKER_TAG,
            ExistingWorkPolicy.REPLACE,
            databaseSyncWorkRequest
        )
    }
}
