package sam.g.trackuriboh


import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import sam.g.trackuriboh.managers.SessionManager
import sam.g.trackuriboh.workers.DatabaseUpdateCheckWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var mSessionManager: SessionManager

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        applicationScope.launch {
            mSessionManager.apply {
                fetchTCGPlayerAccessToken()
            }
        }

        // Check for database updates on the background
        val databaseUpdateRequest = PeriodicWorkRequestBuilder<DatabaseUpdateCheckWorker>(24, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            DatabaseUpdateCheckWorker.WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            databaseUpdateRequest
        )

    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder().setWorkerFactory(workerFactory).build()
}