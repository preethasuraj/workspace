package com.preethasuraj.watchlist.domain.model

/** Price direction vs. the previous close, for movement indicators. */
enum class PriceMovement { UP, DOWN, FLAT, UNKNOWN }

/**
 * A fully-resolved watchlist row for display: the instrument merged with its latest
 * known price (live tick, snapshot, or cached), plus derived movement and staleness.
 *
 * @param price latest known price, or null if none is available yet.
 * @param isStale true when the price is missing or older than the freshness threshold
 * (e.g. the stream is quiet because the market is closed or the connection dropped).
 * @param updatedAt epoch ms of the price, or null if unknown.
 */
data class WatchlistItem(
    val instrument: Instrument,
    val price: Double?,
    val previousClose: Double?,
    val movement: PriceMovement,
    val isStale: Boolean,
    val updatedAt: Long?,
) {
    /** Absolute change vs. the previous close ("today"), or null if either is unknown. */
    val change: Double?
        get() = if (price != null && previousClose != null) price - previousClose else null

    /** Percentage change vs. the previous close, or null if it can't be computed. */
    val changePercent: Double?
        get() = if (price != null && previousClose != null && previousClose != 0.0) {
            (price - previousClose) / previousClose * 100.0
        } else {
            null
        }
}

