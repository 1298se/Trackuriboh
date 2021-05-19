package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.services.DatabaseSyncService
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val databaseSyncService: DatabaseSyncService
) : ViewModel() {
    // Sync
    val databaseSyncState: StateFlow<DatabaseSyncService.DatabaseSyncState> = databaseSyncService.databaseSyncState

    private var syncJob: Job? = null

    fun syncDatabase() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch(Dispatchers.IO) {
            databaseSyncService.syncDatabase()
        }
    }
}
