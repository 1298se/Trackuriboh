package sam.g.trackuriboh.data.repository

import android.content.SharedPreferences
import sam.g.trackuriboh.data.network.interceptors.AuthorizationInterceptor
import sam.g.trackuriboh.data.network.services.AccessTokenApiService
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private const val ACCESS_TOKEN_EXPIRY = "AccessTokenExpiry"
private const val ACCESS_TOKEN = "AccessToken"

@Singleton
class SessionManager @Inject constructor(
    private val accessTokenApiService: AccessTokenApiService,
    private val sharedPreferences: SharedPreferences,
    private val authorizationInterceptor: AuthorizationInterceptor
) {

    suspend fun setAccessToken() {
        val tokenExpiry = sharedPreferences.getString(ACCESS_TOKEN_EXPIRY, null)

        val accessToken = if (tokenExpiry == null || isExpired(tokenExpiry)) {
            val accessTokenResponse = accessTokenApiService.getAccessToken()
            with(sharedPreferences.edit()) {
                putString(ACCESS_TOKEN_EXPIRY, accessTokenResponse.expiry)
                putString(ACCESS_TOKEN, accessTokenResponse.token)
                apply()
            }

            accessTokenResponse.token
        } else {
            sharedPreferences.getString(ACCESS_TOKEN, null) ?: throw IllegalStateException("")
        }

        authorizationInterceptor.accessToken = accessToken
    }

    private fun isExpired(date: String): Boolean {
        val sdf = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.getDefault())
        val curDate = Calendar.getInstance(Locale.getDefault()).time
        val expiryDate = sdf.parse(date) ?: curDate

        return expiryDate <= curDate
    }
}
