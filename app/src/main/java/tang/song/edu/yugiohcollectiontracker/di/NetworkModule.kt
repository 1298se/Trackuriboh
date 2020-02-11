package tang.song.edu.yugiohcollectiontracker.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tang.song.edu.yugiohcollectiontracker.network.CardRetrofitService
import tang.song.edu.yugiohcollectiontracker.network.response.ResponseHandler
import javax.inject.Singleton

@Module
abstract class NetworkModule {
    companion object {
        private const val BASE_URL = "https://db.ygoprodeck.com/api/v6/"

        @Singleton
        @Provides
        fun provideRetrofitInstance(): Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Singleton
        @Provides
        fun provideCardRetrofitService(retrofit: Retrofit): CardRetrofitService =
            retrofit.create(CardRetrofitService::class.java)
    }
}