package sam.g.trackuriboh.data.network.services

import retrofit2.http.GET
import retrofit2.http.Headers
import sam.g.trackuriboh.data.network.interceptors.AUTHORIZATION_HEADER
import sam.g.trackuriboh.data.network.responses.CardRarityResponse
import sam.g.trackuriboh.data.network.responses.ConditionResponse
import sam.g.trackuriboh.data.network.responses.PrintingResponse

/**
 * This fetches additional data such as all supported rarities, printings, conditions
 */
interface CatalogApiService {

    @Headers("$AUTHORIZATION_HEADER: true")
    @GET("rarities")
    suspend fun getCardRarities(): CardRarityResponse

    @Headers("$AUTHORIZATION_HEADER: true")
    @GET("printings")
    suspend fun getPrintings(): PrintingResponse

    @Headers("$AUTHORIZATION_HEADER: true")
    @GET("conditions")
    suspend fun getConditions(): ConditionResponse
}