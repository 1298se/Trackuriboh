package sam.g.trackuriboh.di

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
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

    @Singleton
    @Provides
    fun provideAlarmManager(
        application: Application
    ): AlarmManager = application.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Singleton
    @Provides
    fun provideConnectivityManager(
        application: Application
    ): ConnectivityManager = application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}