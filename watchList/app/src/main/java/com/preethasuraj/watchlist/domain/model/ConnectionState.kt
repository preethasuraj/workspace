package com.preethasuraj.watchlist.domain.model

/** Lifecycle of the live price stream, surfaced to the UI for banners/indicators. */
sealed interface ConnectionState {
    data object Connecting : ConnectionState
    data object Connected : ConnectionState

    /** Dropped and retrying; [attempt] is the 1-based retry count. */
    data class Reconnecting(val attempt: Int) : ConnectionState

    /** Intentionally closed (no subscriptions) or not yet started. */
    data object Disconnected : ConnectionState
}
