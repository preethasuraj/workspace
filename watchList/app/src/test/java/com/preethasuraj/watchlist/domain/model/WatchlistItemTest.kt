package com.preethasuraj.watchlist.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WatchlistItemTest {

    private fun item(price: Double?, previousClose: Double?) = WatchlistItem(
        instrument = Instrument(symbol = "AAPL", displayName = "Apple Inc", type = "Common Stock"),
        price = price,
        previousClose = previousClose,
        movement = PriceMovement.UNKNOWN,
        isStale = false,
        updatedAt = null,
    )

    @Test
    fun `change is the difference from previous close`() {
        assertEquals(-3.61, item(price = 99.69, previousClose = 103.30).change!!, 1e-9)
    }

    @Test
    fun `changePercent is the percentage difference from previous close`() {
        assertEquals(-3.495, item(price = 99.69, previousClose = 103.30).changePercent!!, 1e-3)
    }

    @Test
    fun `change is null when price is unknown`() {
        assertNull(item(price = null, previousClose = 103.30).change)
    }

    @Test
    fun `change is null when previous close is unknown`() {
        assertNull(item(price = 99.69, previousClose = null).change)
    }

    @Test
    fun `changePercent is null when previous close is zero`() {
        assertNull(item(price = 99.69, previousClose = 0.0).changePercent)
    }

    @Test
    fun `a positive move yields a positive change`() {
        assertEquals(2.0, item(price = 201.0, previousClose = 199.0).change!!, 1e-9)
    }
}
