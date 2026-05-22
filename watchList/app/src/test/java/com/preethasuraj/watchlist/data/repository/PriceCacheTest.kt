package com.preethasuraj.watchlist.data.repository

import com.preethasuraj.watchlist.domain.model.PricePoint
import com.preethasuraj.watchlist.domain.model.Quote
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PriceCacheTest {

    private val cache = PriceCache()

    @Test
    fun `seed stores the full quote`() {
        cache.seed(Quote("AAPL", price = 195.0, previousClose = 199.0, updatedAt = 1_000L))

        val quote = cache.prices.value["AAPL"]
        assertEquals(195.0, quote?.price)
        assertEquals(199.0, quote?.previousClose)
        assertEquals(1_000L, quote?.updatedAt)
    }

    @Test
    fun `update applies a tick and preserves the previously-known previous close`() {
        cache.seed(Quote("AAPL", price = 195.0, previousClose = 199.0, updatedAt = 1_000L))

        cache.update(PricePoint("AAPL", price = 196.5, epochMs = 2_000L))

        val quote = cache.prices.value["AAPL"]
        assertEquals(196.5, quote?.price)
        assertEquals(199.0, quote?.previousClose)
        assertEquals(2_000L, quote?.updatedAt)
    }

    @Test
    fun `update on an unseeded symbol leaves previous close unknown`() {
        cache.update(PricePoint("TSLA", price = 250.0, epochMs = 3_000L))

        val quote = cache.prices.value["TSLA"]
        assertEquals(250.0, quote?.price)
        assertNull(quote?.previousClose)
    }

    @Test
    fun `remove drops the symbol`() {
        cache.seed(Quote("AAPL", price = 195.0, previousClose = 199.0, updatedAt = 1_000L))

        cache.remove("AAPL")

        assertTrue(cache.prices.value.isEmpty())
    }

    @Test
    fun `seed overwrites an existing entry`() {
        cache.seed(Quote("AAPL", price = 195.0, previousClose = 199.0, updatedAt = 1_000L))
        cache.seed(Quote("AAPL", price = 201.0, previousClose = 200.0, updatedAt = 5_000L))

        val quote = cache.prices.value["AAPL"]
        assertEquals(201.0, quote?.price)
        assertEquals(200.0, quote?.previousClose)
        assertEquals(5_000L, quote?.updatedAt)
    }
}
