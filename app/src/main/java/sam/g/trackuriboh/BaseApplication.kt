package sam.g.trackuriboh

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.repository.CardSetRepository
import sam.g.trackuriboh.data.repository.ProductRepository
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

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var cardSetRepository: CardSetRepository

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        with(workRequestManager) {
            enqueueDatabaseUpdateCheck()
            enqueuePeriodicDatabaseUpdateCheckScheduler()
            enqueuePeriodicPriceSync()
        }

        MainScope().launch {
            checkIfForceUpdateRequired()
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder().setWorkerFactory(workerFactory).build()

    private suspend fun checkIfForceUpdateRequired() {
        if (productRepository.getTotalCardCount() == 0 ||
                cardSetRepository.getTotalCardSetCount() == 0) {
            workRequestManager.enqueueDatabaseDownloadWorker()
        }
    }
}