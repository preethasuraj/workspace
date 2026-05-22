package com.preethasuraj.watchlist.data.local

import com.preethasuraj.watchlist.domain.model.Instrument

/** Builds a fresh entity for insertion; price fields start null and are filled by a snapshot. */
fun Instrument.toEntity(addedAt: Long): WatchedInstrumentEntity =
    WatchedInstrumentEntity(
        symbol = symbol,
        displayName = displayName,
        type = type,
        addedAt = addedAt,
        lastPrice = null,
        lastPriceAt = null,
    )
