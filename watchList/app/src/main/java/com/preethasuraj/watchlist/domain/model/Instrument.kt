package com.preethasuraj.watchlist.domain.model

/**
 * A tradable instrument the user can search for and add to their watchlist.
 *
 * @param symbol the canonical Finnhub symbol used for quotes and streaming (e.g. "AAPL").
 * @param displayName a human-readable label (company description or display symbol).
 * @param type instrument type as reported by Finnhub (e.g. "Common Stock"); may be blank.
 */
data class Instrument(
    val symbol: String,
    val displayName: String,
    val type: String,
)
