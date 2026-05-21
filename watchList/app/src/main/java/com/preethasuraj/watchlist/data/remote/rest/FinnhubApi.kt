package com.preethasuraj.watchlist.data.remote.rest

import com.preethasuraj.watchlist.data.remote.rest.dto.QuoteDto
import com.preethasuraj.watchlist.data.remote.rest.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Finnhub REST endpoints. The `token` query parameter is appended automatically by
 * [com.preethasuraj.watchlist.data.remote.AuthInterceptor], so callers never pass it.
 */
interface FinnhubApi {

    @GET("api/v1/search")
    suspend fun search(@Query("q") query: String): SearchResponseDto

    @GET("api/v1/quote")
    suspend fun quote(@Query("symbol") symbol: String): QuoteDto
}
