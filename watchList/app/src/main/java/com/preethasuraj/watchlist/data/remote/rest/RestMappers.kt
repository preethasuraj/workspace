package com.preethasuraj.watchlist.data.remote.rest

import com.preethasuraj.watchlist.data.remote.rest.dto.QuoteDto
import com.preethasuraj.watchlist.data.remote.rest.dto.SearchResultDto
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.model.Quote

/**
 * Maps a search result to a domain [Instrument], or null if it has no usable symbol.
 * Falls back to the display symbol and then the raw symbol for the display name.
 */
fun SearchResultDto.toInstrumentOrNull(): Instrument? {
    val resolvedSymbol = symbol?.takeIf { it.isNotBlank() } ?: return null
    val name = description?.takeIf { it.isNotBlank() }
        ?: displaySymbol?.takeIf { it.isNotBlank() }
        ?: resolvedSymbol
    return Instrument(
        symbol = resolvedSymbol,
        displayName = name,
        type = type.orEmpty(),
    )
}

/**
 * Maps a quote payload to a domain [Quote], or null when Finnhub reports no data
 * (an all-zero response). The symbol is supplied by the caller since `/quote` doesn't
 * echo it back. Timestamps are converted from epoch seconds to epoch milliseconds.
 */
fun QuoteDto.toQuoteOrNull(symbol: String): Quote? {
    val hasNoData = current == 0.0 && previousClose == 0.0 && timestamp == 0L
    if (hasNoData) return null
    return Quote(
        symbol = symbol,
        price = current,
        previousClose = previousClose.takeIf { it != 0.0 },
        updatedAt = if (timestamp > 0L) timestamp * 1000L else System.currentTimeMillis(),
    )
}
