package tang.song.edu.yugiohcollectiontracker.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import tang.song.edu.yugiohcollectiontracker.data.db.CardDatabase
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideCardDatabase(application: Application): CardDatabase =
        CardDatabase(application)
}
