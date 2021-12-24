package sam.g.trackuriboh


import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import sam.g.trackuriboh.managers.SessionManager
import sam.g.trackuriboh.workers.WorkRequestManager
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var mSessionManager: SessionManager

    @Inject
    lateinit var workRequestManager: WorkRequestManager

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        workRequestManager.enqueuePeriodicDatabaseUpdateCheck()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder().setWorkerFactory(workerFactory).build()
}