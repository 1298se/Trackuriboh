package sam.g.trackuriboh.data.network.services

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import sam.g.trackuriboh.data.network.responses.AccessTokenResponse

interface AccessTokenApiService {

    @POST("token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String = "dfe8663b-1fee-41c5-a8be-095a4d4aa765",
        @Field("client_secret") clientSecret: String = "8f14e005-c8d2-45df-9454-409a4b79b619"
    ): AccessTokenResponse
}
