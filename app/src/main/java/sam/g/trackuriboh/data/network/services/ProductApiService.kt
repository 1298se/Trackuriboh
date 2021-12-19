package sam.g.trackuriboh.data.network.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerAuthorizationInterceptor
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerCategoryInterceptor.Companion.CATEGORY_QUERY_PARAM
import sam.g.trackuriboh.data.network.responses.CardResponse

interface ProductApiService {

    @Headers("${TCGPlayerAuthorizationInterceptor.AUTHORIZATION_HEADER}: true", "$CATEGORY_QUERY_PARAM: true")
    @GET("catalog/products?getExtendedFields=true&includeSkus=true")
    suspend fun getProducts(
        @Query( "productTypes") productTypes: List<String>,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("groupId") cardSetId: Long?
    ): Response<CardResponse>
}
