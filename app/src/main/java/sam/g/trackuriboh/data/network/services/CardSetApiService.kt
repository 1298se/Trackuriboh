package sam.g.trackuriboh.data.network.services

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerAuthorizationInterceptor
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerCategoryInterceptor.Companion.CATEGORY_QUERY_PARAM
import sam.g.trackuriboh.data.network.responses.CardSetResponse
import sam.g.trackuriboh.di.NetworkModule.DEFAULT_QUERY_LIMIT

interface CardSetApiService {

    @Headers("${TCGPlayerAuthorizationInterceptor.AUTHORIZATION_HEADER}: true", "$CATEGORY_QUERY_PARAM: true")
    @GET("catalog/groups")
    suspend fun getSets(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = DEFAULT_QUERY_LIMIT,
    ): CardSetResponse
}
