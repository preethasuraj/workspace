package com.preethasuraj.watchlist.data.remote.ws

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Inbound WebSocket frame. `type` is "trade" for price ticks (with [data]) or "ping" for
 * keepalives (no data). Unknown fields are ignored by the shared Json config.
 */
@Serializable
data class TradeMessage(
    val type: String,
    val data: List<TradeTick>? = null,
)

@Serializable
data class TradeTick(
    @SerialName("s") val symbol: String,
    @SerialName("p") val price: Double,
    @SerialName("t") val timestamp: Long,
)
