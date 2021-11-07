package sam.g.trackuriboh.data.network.services

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import sam.g.trackuriboh.data.network.interceptors.AUTHORIZATION_HEADER
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerCategoryInterceptor.Companion.CATEGORY_QUERY_PARAM
import sam.g.trackuriboh.data.network.responses.CardResponse
import sam.g.trackuriboh.data.network.responses.SkuResponse
import sam.g.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT

interface ProductApiService {

    companion object {
        private const val TCGPLAYER_PRODUCT_TYPE_CARDS = "Cards"
    }

    @Headers("$AUTHORIZATION_HEADER: true", "$CATEGORY_QUERY_PARAM: true")
    @GET("catalog/products?productTypes=$TCGPLAYER_PRODUCT_TYPE_CARDS&getExtendedFields=true&includeSkus=true")
    suspend fun getCards(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = DEFAULT_QUERY_LIMIT,
    ): CardResponse

    @Headers("$AUTHORIZATION_HEADER: true", "$CATEGORY_QUERY_PARAM: true")
    @GET("catalog/products/{productId}/skus")
    suspend fun getProductSkus(
        @Path(value = "productId", encoded = true) productId: Long
    ): SkuResponse
}
