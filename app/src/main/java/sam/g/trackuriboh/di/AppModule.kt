package sam.g.trackuriboh.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sam.g.trackuriboh.R
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(
        application: Application
    ): SharedPreferences =
        application.getSharedPreferences(application.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideWorkManager(
        application: Application
    ): WorkManager =
        WorkManager.getInstance(application)
}