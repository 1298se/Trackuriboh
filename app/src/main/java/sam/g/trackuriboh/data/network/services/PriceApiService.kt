package sam.g.trackuriboh.data.network.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import sam.g.trackuriboh.data.network.interceptors.TCGPlayerAuthorizationInterceptor
import sam.g.trackuriboh.data.network.responses.SkuPriceResponse

interface PriceApiService {
    companion object {
        const val PRICE_URL = "pricing/"
    }

    @Headers("${TCGPlayerAuthorizationInterceptor.AUTHORIZATION_HEADER}: true")
    @GET("${PRICE_URL}sku/{skuIds}")
    suspend fun getPricesForSkus(
        // List of comma delimited skuIds
        @Path(value="skuIds", encoded = true) skuIds: String
    ): Response<SkuPriceResponse>
}