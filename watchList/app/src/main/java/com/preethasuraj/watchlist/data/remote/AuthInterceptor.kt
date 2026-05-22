package com.preethasuraj.watchlist.data.remote

import com.preethasuraj.watchlist.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Appends the Finnhub `token` query parameter to every outgoing request, so the API key
 * lives in exactly one place and never appears at call sites. The key is supplied at
 * build time via [BuildConfig.FINNHUB_API_KEY] (read from local.properties).
 */
class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        // The WebSocket handshake already carries its own token; don't add a duplicate.
        if (original.url.queryParameter("token") != null) {
            return chain.proceed(original)
        }
        val url = original.url.newBuilder()
            .addQueryParameter("token", BuildConfig.FINNHUB_API_KEY)
            .build()
        return chain.proceed(original.newBuilder().url(url).build())
    }
}
