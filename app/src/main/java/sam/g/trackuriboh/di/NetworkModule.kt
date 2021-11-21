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
import sam.g.trackuriboh.data.network.services.*
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val DEFAULT_QUERY_LIMIT = 100
    const val TCGPLAYER_YUGIOH_CATEGORY_ID = 2
    const val TCGPLAYER_PRODUCT_URL = "https://www.tcgplayer.com/product/"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CategoryRetrofitInstance

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PriceRetrofitInstance

    private const val TCGPLAYER_API_VERSION = "v1.39.0"
    private const val TCGPLAYER_API_BASE_URL = "https://api.tcgplayer.com/$TCGPLAYER_API_VERSION/"
    private const val TCGPLAYER_API_CATEGORIES_URL = "${TCGPLAYER_API_BASE_URL}catalog/categories/$TCGPLAYER_YUGIOH_CATEGORY_ID/"
    private const val TCGPLAYER_API_PRICES_URL = "${TCGPLAYER_API_BASE_URL}pricing/"

    @Singleton
    @Provides
    fun provideAuthorizationInterceptor(): TCGPlayerAuthorizationInterceptor =
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
            .build()

    @Singleton
    @Provides
    fun provideBaseRetrofitInstance(okHttpClient: OkHttpClient, converterFactory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl(TCGPLAYER_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    @CategoryRetrofitInstance
    @Singleton
    @Provides
    fun provideCategoryRetrofitIntance(okHttpClient: OkHttpClient, converterFactory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl(TCGPLAYER_API_CATEGORIES_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    @PriceRetrofitInstance
    @Singleton
    @Provides
    fun providePriceRetrofitInstance(okHttpClient: OkHttpClient, converterFactory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl(TCGPLAYER_API_PRICES_URL)
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
    fun provideCatalogApiService(@CategoryRetrofitInstance retrofit: Retrofit): CatalogApiService =
        retrofit.create(CatalogApiService::class.java)

    @Singleton
    @Provides
    fun providePriceApiService(@PriceRetrofitInstance retrofit: Retrofit): PriceApiService =
        retrofit.create(PriceApiService::class.java)
}