package tang.song.edu.yugiohcollectiontracker.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import tang.song.edu.yugiohcollectiontracker.ui_card_detail.CardDetailFragment
import tang.song.edu.yugiohcollectiontracker.ui_card_set_detail.CardSetDetailFragment
import tang.song.edu.yugiohcollectiontracker.ui_database.CardListFragment
import tang.song.edu.yugiohcollectiontracker.ui_database.CardSetListFragment
import tang.song.edu.yugiohcollectiontracker.ui_database.DatabaseFragment
import tang.song.edu.yugiohcollectiontracker.workers.DatabaseSyncWorker
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, RoomModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(fragment: CardListFragment)
    fun inject(fragment: CardSetListFragment)
    fun inject(fragment: DatabaseFragment)
    fun inject(fragment: CardDetailFragment)
    fun inject(fragment: CardSetDetailFragment)

    fun inject(worker: DatabaseSyncWorker)
}