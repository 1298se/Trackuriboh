package sam.g.trackuriboh.ui_database.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sam.g.trackuriboh.services.DatabaseSyncWorker
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val workManager: WorkManager,
) : ViewModel() {

    val databaseSyncState: LiveData<DatabaseSyncWorker.DatabaseSyncState>
        get() = _databaseSyncState
    private val _databaseSyncState = MutableLiveData<DatabaseSyncWorker.DatabaseSyncState>()

    private var syncJob: Job? = null

    fun syncDatabase() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            val databaseSyncWorkRequest = OneTimeWorkRequestBuilder<DatabaseSyncWorker>()
                .build()

            workManager.enqueueUniqueWork(DatabaseSyncWorker.WORKER_TAG, ExistingWorkPolicy.REPLACE, databaseSyncWorkRequest)
        }
    }
}
