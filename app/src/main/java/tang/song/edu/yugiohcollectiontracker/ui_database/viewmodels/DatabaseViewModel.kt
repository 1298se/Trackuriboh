package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import tang.song.edu.yugiohcollectiontracker.workers.DatabaseSyncWorker

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {
    private val workManager: WorkManager = WorkManager.getInstance(application)

    fun syncDatabase() {
        val uploadWorkRequest = OneTimeWorkRequestBuilder<DatabaseSyncWorker>().build()

        workManager.enqueue(uploadWorkRequest)
    }
}
