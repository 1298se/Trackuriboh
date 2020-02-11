package tang.song.edu.yugiohcollectiontracker.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import tang.song.edu.yugiohcollectiontracker.MainActivity
import tang.song.edu.yugiohcollectiontracker.ui_home.HomeFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: HomeFragment)
}