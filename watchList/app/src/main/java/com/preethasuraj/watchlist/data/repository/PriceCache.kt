package com.preethasuraj.watchlist.data.repository

import com.preethasuraj.watchlist.domain.model.PricePoint
import com.preethasuraj.watchlist.domain.model.Quote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory, latest-price-per-symbol cache. Seeded by REST snapshots and updated by live
 * ticks. This is the hot path, so prices never touch the database here. A tick preserves
 * the symbol's previously-known [Quote.previousClose] (ticks don't carry it) so movement
 * stays computable.
 */
@Singleton
class PriceCache @Inject constructor() {

    private val _prices = MutableStateFlow<Map<String, Quote>>(emptyMap())
    val prices: StateFlow<Map<String, Quote>> = _prices.asStateFlow()

    /** Seeds/overwrites a full quote (from a REST snapshot). */
    fun seed(quote: Quote) {
        _prices.update { it + (quote.symbol to quote) }
    }

    /** Applies a live tick, keeping the previously-known previous close. */
    fun update(point: PricePoint) {
        _prices.update { current ->
            val previousClose = current[point.symbol]?.previousClose
            current + (point.symbol to Quote(point.symbol, point.price, previousClose, point.epochMs))
        }
    }

    fun remove(symbol: String) {
        _prices.update { it - symbol }
    }
}
