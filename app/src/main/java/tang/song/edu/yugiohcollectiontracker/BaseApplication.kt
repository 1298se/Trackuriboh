package tang.song.edu.yugiohcollectiontracker


import android.app.Application
import com.facebook.stetho.Stetho
import tang.song.edu.yugiohcollectiontracker.di.AppComponent
import tang.song.edu.yugiohcollectiontracker.di.DaggerAppComponent

class BaseApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}