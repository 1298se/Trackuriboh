package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import tang.song.edu.yugiohcollectiontracker.TAG_DATABASE_SYNC_WORK
import tang.song.edu.yugiohcollectiontracker.workers.DatabaseSyncWorker

class DatabaseViewModel @ViewModelInject constructor(
    application: Application,
) : ViewModel() {
    // Sync
    val syncWorkInfo: LiveData<List<WorkInfo>>
    private val workManager: WorkManager = WorkManager.getInstance(application)

    init {
        syncWorkInfo = workManager.getWorkInfosForUniqueWorkLiveData(TAG_DATABASE_SYNC_WORK)
    }

    fun syncDatabase() {
        val uploadWorkRequest = OneTimeWorkRequestBuilder<DatabaseSyncWorker>()
            .addTag(TAG_DATABASE_SYNC_WORK)
            .build()

        workManager.enqueueUniqueWork(
            TAG_DATABASE_SYNC_WORK,
            ExistingWorkPolicy.KEEP,
            uploadWorkRequest
        )
    }
}
