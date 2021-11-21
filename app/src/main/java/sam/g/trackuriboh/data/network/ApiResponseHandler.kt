package sam.g.trackuriboh.data.network

import retrofit2.Response
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse
import sam.g.trackuriboh.data.network.responses.Resource
import java.io.IOException
import javax.inject.Inject

class ApiResponseHandler @Inject constructor() {
     suspend fun <T: Any> getResource(call: suspend () -> Response<T>): Resource<T> {
         return try {
            val response = call()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                Resource.Success(body)
            } else {
                Resource.Failure(IOException(response.message()))
            }
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    suspend fun <T: BaseTCGPlayerResponse<*>> getTCGPlayerResource(call: suspend () -> Response<T>): Resource<T> {
        val resource = getResource(call)

        // If it's successful but there are errors, then return failure. Else, just return the resource
        return if (resource is Resource.Success && resource.data?.errors?.isNotEmpty() == true) {
            Resource.Failure(IOException(resource.data.errors.first()))
        } else {
            resource
        }
    }
}