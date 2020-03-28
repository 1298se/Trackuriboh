package tang.song.edu.yugiohcollectiontracker.di

import android.app.Application
import dagger.Module
import dagger.Provides
import tang.song.edu.yugiohcollectiontracker.data.db.CardDatabase
import tang.song.edu.yugiohcollectiontracker.data.db.dao.CardDao
import javax.inject.Singleton

@Module
object RoomModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideCardDatabase(application: Application): CardDatabase =
        CardDatabase(application)

    @JvmStatic
    @Provides
    fun providesCardDao(cardDatabase: CardDatabase): CardDao = cardDatabase.cardDao()
}
