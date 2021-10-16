package tang.song.edu.trackuriboh.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

const val AUTHORIZATION_HEADER = "Authorization"

class AuthorizationInterceptor : Interceptor {
    var accessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        if (request.header(AUTHORIZATION_HEADER) != null) {
            if (accessToken == null) {
                throw IllegalAccessException("ACCESS TOKEN REQUIRED BUT IS NULL")
            } else {
                requestBuilder.header(AUTHORIZATION_HEADER, "bearer $accessToken")
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}
