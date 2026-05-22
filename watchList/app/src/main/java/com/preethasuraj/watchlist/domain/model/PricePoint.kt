package com.preethasuraj.watchlist.domain.model

/**
 * A single live trade tick from the stream.
 *
 * @param epochMs trade time in epoch milliseconds (Finnhub's WebSocket `t` is already ms,
 * unlike the REST quote whose `t` is seconds).
 */
data class PricePoint(
    val symbol: String,
    val price: Double,
    val epochMs: Long,
)
