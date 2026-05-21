package com.preethasuraj.watchlist.domain.model

/**
 * A watchlist entry: the instrument, when it was added, and its last known price (if any).
 *
 * The last known price is the persisted snapshot used for immediate/offline display.
 * Live price merging and staleness/movement are layered on top in the presentation
 * layer once the streaming source is in place.
 */
data class WatchedInstrument(
    val instrument: Instrument,
    val addedAt: Long,
    val lastPrice: Double?,
    val lastPriceAt: Long?,
)
