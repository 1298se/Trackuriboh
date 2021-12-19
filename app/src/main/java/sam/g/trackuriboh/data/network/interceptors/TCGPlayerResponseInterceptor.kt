package sam.g.trackuriboh.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME

class TCGPlayerResponseInterceptor : Interceptor {
    companion object {
        private const val NO_RESULTS_ERROR_MESSAGE = "No products were found."
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())

        // The API currently returns 404 for queries with no results. This isn't very convenient because it counts
        // as an exception to retrofit. So if there are no results, we just change it to a 200.
        if (response.code() == 404) {
            val responseBody = response.body()

            if (responseBody != null) {
                val jsonString = responseBody.charStream().readText()

                val jsonObject = JSONObject(jsonString)
                val errors = jsonObject.optJSONArray(ERROR_FIELD_NAME)

                if (errors?.length() != 0 && errors?.get(0) == NO_RESULTS_ERROR_MESSAGE) {
                    jsonObject.put(ERROR_FIELD_NAME, JSONArray())

                    return response.newBuilder()
                        .body(
                            ResponseBody.create(responseBody.contentType(), jsonObject.toString())
                        )
                        .code(200).build()
                }
            }
        }

        return response
    }

}