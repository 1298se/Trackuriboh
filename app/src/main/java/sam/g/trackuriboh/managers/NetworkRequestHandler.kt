package sam.g.trackuriboh.managers

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.Response
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse
import sam.g.trackuriboh.data.network.responses.Resource
import java.io.IOException
import javax.inject.Inject

class NetworkRequestHandler @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val sessionManager: SessionManager,

) {

    suspend fun <T: BaseTCGPlayerResponse<*>> getTCGPlayerResource(call: suspend () -> Response<T>): Resource<T> {
        try {
            if (!isConnectedToNetwork()) {
                return Resource.Offline
            }

            sessionManager.setTCGPlayerAccessTokenIfNecessary()

            val response = call()
            val body = response.body()

            // If the errors array is not empty, the request failed
            if (body != null && body.errors.isNotEmpty()) {
                return Resource.Failure(IOException(body.errors.first()))
            }

            if (body != null && response.isSuccessful) {
                return Resource.Success(body)
            }

            return Resource.Failure(IOException(response.code().toString()))
        } catch (e: Exception) {
            return Resource.Failure(e)
        }
    }

    private fun isConnectedToNetwork(): Boolean {
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}