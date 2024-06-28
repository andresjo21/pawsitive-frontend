package cr.una.pawsitive.utils

import cr.una.pawsitive.utils.MyApplication.Companion.sessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager?.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", it)
        }

        return chain.proceed(requestBuilder.build())
    }
}