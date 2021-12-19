package sam.g.trackuriboh.data.network.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerAuthorizationInterceptor
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerCategoryInterceptor.Companion.CATEGORY_QUERY_PARAM
import sam.g.trackuriboh.data.network.responses.CardSetResponse

interface CardSetApiService {

    @Headers("${TCGPlayerAuthorizationInterceptor.AUTHORIZATION_HEADER}: true", "$CATEGORY_QUERY_PARAM: true")
    @GET("catalog/groups")
    suspend fun getCardSets(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Response<CardSetResponse>

    @Headers("${TCGPlayerAuthorizationInterceptor.AUTHORIZATION_HEADER}: true")
    @GET("catalog/groups/{groupIds}")
    suspend fun getCardSetDetails(
        // List of comma delimited groupIds
        @Path(value="groupIds", encoded = true) groupIds: String,
    ): Response<CardSetResponse>
}
