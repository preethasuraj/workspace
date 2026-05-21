package com.preethasuraj.watchlist.domain.model

/**
 * The latest known price for an instrument.
 *
 * Seeded from a REST snapshot and then continuously refreshed by the trade stream.
 * [updatedAt] drives both staleness detection and price-movement indicators, so it is
 * always stored in epoch milliseconds regardless of the source's native unit.
 *
 * @param previousClose the prior session close, when known; used to compute movement.
 * @param updatedAt epoch milliseconds of the price observation.
 */
data class Quote(
    val symbol: String,
    val price: Double,
    val previousClose: Double?,
    val updatedAt: Long,
)
