package tang.song.edu.yugiohcollectiontracker.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tang.song.edu.yugiohcollectiontracker.data.db.CardDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideCardDatabase(application: Application): CardDatabase = CardDatabase(application)
}
