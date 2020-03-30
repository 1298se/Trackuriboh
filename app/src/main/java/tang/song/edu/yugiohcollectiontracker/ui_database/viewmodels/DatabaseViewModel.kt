package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import tang.song.edu.yugiohcollectiontracker.TAG_DATABASE_SYNC_WORK
import tang.song.edu.yugiohcollectiontracker.workers.DatabaseSyncWorker

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {
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
