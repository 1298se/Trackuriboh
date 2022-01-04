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
import dagger.hilt.android.qualifiers.ApplicationContext
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
        @ApplicationContext applicationContext: Context
    ): WorkManager =
        WorkManager.getInstance(applicationContext)

    @Singleton
    @Provides
    fun provideAlarmManager(
        @ApplicationContext applicationContext: Context
    ): AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext applicationContext: Context
    ): ConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}