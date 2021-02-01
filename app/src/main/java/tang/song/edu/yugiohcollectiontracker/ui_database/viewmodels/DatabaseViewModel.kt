package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.services.DatabaseSyncService

class DatabaseViewModel @ViewModelInject constructor(
    private val databaseSyncService: DatabaseSyncService
) : ViewModel() {
    // Sync
    val databaseSyncState: StateFlow<DatabaseSyncService.DatabaseSyncState> = databaseSyncService.databaseSyncState

    private var syncJob: Job? = null

    fun syncDatabase() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            databaseSyncService.syncDatabase()
        }
    }
}
