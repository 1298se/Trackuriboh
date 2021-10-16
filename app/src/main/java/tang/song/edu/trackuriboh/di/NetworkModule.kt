package tang.song.edu.trackuriboh.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tang.song.edu.trackuriboh.data.network.interceptors.AuthorizationInterceptor
import tang.song.edu.trackuriboh.data.network.interceptors.TCGPlayerCategoryInterceptor
import tang.song.edu.trackuriboh.data.network.services.AccessTokenApiService
import tang.song.edu.trackuriboh.data.network.services.CardApiService
import tang.song.edu.trackuriboh.data.network.services.CardSetApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val DEFAULT_QUERY_LIMIT = 100
    private const val API_VERSION = "v1.39.0"
    private const val BASE_URL = "https://api.tcgplayer.com/$API_VERSION/"

    @Singleton
    @Provides
    fun provideAuthorizationInterceptor(): AuthorizationInterceptor =
        AuthorizationInterceptor()

    @Singleton
    @Provides
    fun provideOkHttpClient(authorizationInterceptor: AuthorizationInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(TCGPlayerCategoryInterceptor())
            .build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideCardRetrofitService(retrofit: Retrofit): CardApiService =
        retrofit.create(CardApiService::class.java)

    @Singleton
    @Provides
    fun provideAccessTokenRetrofitService(retrofit: Retrofit): AccessTokenApiService =
        retrofit.create(AccessTokenApiService::class.java)

    @Singleton
    @Provides
    fun provideCardSetWebService(retrofit: Retrofit): CardSetApiService =
        retrofit.create(CardSetApiService::class.java)
}