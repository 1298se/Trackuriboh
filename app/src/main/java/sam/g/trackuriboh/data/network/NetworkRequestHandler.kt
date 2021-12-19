package sam.g.trackuriboh.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import retrofit2.Response
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse
import sam.g.trackuriboh.data.network.responses.Resource
import java.io.IOException
import javax.inject.Inject

class NetworkRequestHandler @Inject constructor(
    private val connectivityManager: ConnectivityManager,
) {

    suspend fun <T: BaseTCGPlayerResponse<*>> getTCGPlayerResource(call: suspend () -> Response<T>): Resource<T> {
        try {
            if (!isConnectedToNetwork()) {
                return Resource.Offline
            }

            val response = call()
            val body = response.body()

            if (body != null && body.errors.isNotEmpty()) {
                return Resource.Failure(IOException(body.errors.first()))
            }

            if (response.isSuccessful && body != null) {
                return Resource.Success(body)
            }

            return Resource.Failure(IOException("fAIL"))
        } catch (e: Exception) {
            return Resource.Failure(e)
        }
    }

    private fun isConnectedToNetwork(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false

            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }
}