package sam.g.trackuriboh.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class TCGPlayerCategoryInterceptor : Interceptor {
    companion object {
        const val CATEGORY_QUERY_PARAM = "Category"
        private const val TCGPLAYER_YUGIOH_CATEGORY_ID = 2
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        var url = request.url()

        if (request.header(CATEGORY_QUERY_PARAM) != null) {
            requestBuilder.removeHeader(CATEGORY_QUERY_PARAM)

            url = url.newBuilder()
                .addQueryParameter("categoryId", TCGPLAYER_YUGIOH_CATEGORY_ID.toString())
                .build()
        }

        requestBuilder.url(url)

        return chain.proceed(requestBuilder.build())
    }
}