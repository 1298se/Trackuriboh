package tang.song.edu.trackuriboh.data.network.services

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import tang.song.edu.trackuriboh.data.network.interceptors.AUTHORIZATION_HEADER
import tang.song.edu.trackuriboh.data.network.interceptors.TCGPlayerCategoryInterceptor.Companion.CATEGORY_QUERY_PARAM
import tang.song.edu.trackuriboh.data.network.responses.CardResponse
import tang.song.edu.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT

const val TCGPLAYER_PRODUCT_TYPE_CARDS = "Cards"
interface CardApiService {

    @Headers("$AUTHORIZATION_HEADER: true", "$CATEGORY_QUERY_PARAM: true")
    @GET("catalog/products?productTypes=$TCGPLAYER_PRODUCT_TYPE_CARDS&getExtendedFields=true")
    suspend fun getCards(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = DEFAULT_QUERY_LIMIT,
    ): CardResponse
}
