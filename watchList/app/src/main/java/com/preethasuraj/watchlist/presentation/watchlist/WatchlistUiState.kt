package com.preethasuraj.watchlist.presentation.watchlist

import com.preethasuraj.watchlist.domain.model.WatchedInstrument

/**
 * Immutable state for the watchlist screen.
 *
 * Loading is the brief initial state before the first DB emission; empty is
 * `!isLoading && items.isEmpty()`; content is a non-empty [items]. Live price updates,
 * connection status, and per-row staleness are layered on in the streaming phase.
 */
data class WatchlistUiState(
    val items: List<WatchedInstrument> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)
