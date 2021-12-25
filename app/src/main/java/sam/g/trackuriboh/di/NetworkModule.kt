package sam.g.trackuriboh.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerAuthorizationInterceptor
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerCategoryInterceptor
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerResponseInterceptor
import sam.g.trackuriboh.data.network.services.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val DEFAULT_QUERY_LIMIT = 100
    const val MAX_PARALLEL_REQUESTS = 10

    const val TCGPLAYER_YUGIOH_CATEGORY_ID = 2
    const val TCGPLAYER_PRODUCT_URL = "https://www.tcgplayer.com/product/"

    private const val TCGPLAYER_API_VERSION = "v1.39.0"
    private const val TCGPLAYER_API_BASE_URL = "https://api.tcgplayer.com/$TCGPLAYER_API_VERSION/"

    @Singleton
    @Provides
    fun provideTCGPlayerAuthorizationInterceptor(): TCGPlayerAuthorizationInterceptor =
        TCGPlayerAuthorizationInterceptor()

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideOkHttpClient(TCGPlayerAuthorizationInterceptor: TCGPlayerAuthorizationInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(TCGPlayerAuthorizationInterceptor)
            .addInterceptor(TCGPlayerCategoryInterceptor())
            .addInterceptor(TCGPlayerResponseInterceptor())
            .build()

    @Singleton
    @Provides
    fun provideBaseRetrofitInstance(okHttpClient: OkHttpClient, converterFactory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl(TCGPLAYER_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    @Singleton
    @Provides
    fun provideProductApiService(retrofit: Retrofit): ProductApiService =
        retrofit.create(ProductApiService::class.java)

    @Singleton
    @Provides
    fun provideAccessTokenApiService(retrofit: Retrofit): AccessTokenApiService =
        retrofit.create(AccessTokenApiService::class.java)

    @Singleton
    @Provides
    fun provideCardSetApiService(retrofit: Retrofit): CardSetApiService =
        retrofit.create(CardSetApiService::class.java)

    @Singleton
    @Provides
    fun provideCatalogApiService(retrofit: Retrofit): CatalogApiService =
        retrofit.create(CatalogApiService::class.java)

    @Singleton
    @Provides
    fun providePriceApiService(retrofit: Retrofit): PriceApiService =
        retrofit.create(PriceApiService::class.java)
}