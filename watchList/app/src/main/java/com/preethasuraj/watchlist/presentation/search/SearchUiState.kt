package com.preethasuraj.watchlist.presentation.search

import com.preethasuraj.watchlist.domain.model.Instrument

/** A single search result row, plus whether it's already on the watchlist. */
data class SearchResultUi(
    val instrument: Instrument,
    val isWatched: Boolean,
)

/**
 * Immutable state for the search screen. The combination of fields expresses every
 * required state: loading (isSearching), empty (results empty, not searching, no error,
 * non-blank query), error (error != null), and content (results non-empty).
 */
data class SearchUiState(
    val query: String = "",
    val results: List<SearchResultUi> = emptyList(),
    val isSearching: Boolean = false,
    val error: String? = null,
)
