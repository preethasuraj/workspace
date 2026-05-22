package com.preethasuraj.watchlist.presentation.watchlist

import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.WatchlistItem

/**
 * Immutable state for the watchlist screen.
 *
 * Loading is the brief initial state before the first emission; empty is
 * `!isLoading && items.isEmpty()`; content is a non-empty [items]. [connection] drives the
 * reconnecting/offline banner, and each [WatchlistItem] carries its own staleness/movement.
 */
data class WatchlistUiState(
    val items: List<WatchlistItem> = emptyList(),
    val isLoading: Boolean = true,
    val connection: ConnectionState = ConnectionState.Disconnected,
    val error: String? = null,
)
