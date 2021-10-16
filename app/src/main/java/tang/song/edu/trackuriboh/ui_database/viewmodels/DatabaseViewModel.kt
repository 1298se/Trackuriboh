package tang.song.edu.trackuriboh.ui_database.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tang.song.edu.trackuriboh.services.DatabaseSyncService
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val databaseSyncService: DatabaseSyncService
) : ViewModel() {

    val databaseSyncState: LiveData<DatabaseSyncService.DatabaseSyncState>
        get() = _databaseSyncState
    private val _databaseSyncState = MutableLiveData<DatabaseSyncService.DatabaseSyncState>()

    private var syncJob: Job? = null

    fun syncDatabase() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            databaseSyncService.syncDatabase().collect { _databaseSyncState.value = it }
        }
    }
}
