package sam.g.trackuriboh.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(
        @ApplicationContext applicationContext: Context
    ) = FirebaseAnalytics.getInstance(applicationContext)

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics() = FirebaseCrashlytics.getInstance()
}