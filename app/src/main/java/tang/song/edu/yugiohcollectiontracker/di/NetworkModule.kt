package tang.song.edu.yugiohcollectiontracker.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService
import javax.inject.Singleton

@Module
object NetworkModule {

    private const val BASE_URL = "https://db.ygoprodeck.com/api/v6/"

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideCardRetrofitService(retrofit: Retrofit): CardRetrofitService =
        retrofit.create(CardRetrofitService::class.java)
}