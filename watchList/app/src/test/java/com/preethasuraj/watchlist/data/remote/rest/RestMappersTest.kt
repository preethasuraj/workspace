package com.preethasuraj.watchlist.data.remote.rest

import com.preethasuraj.watchlist.data.remote.rest.dto.QuoteDto
import com.preethasuraj.watchlist.data.remote.rest.dto.SearchResultDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RestMappersTest {

    @Test
    fun `toInstrumentOrNull returns null when symbol is missing`() {
        val result = SearchResultDto(symbol = null, description = "Apple Inc").toInstrumentOrNull()
        assertNull(result)
    }

    @Test
    fun `toInstrumentOrNull returns null when symbol is blank`() {
        val result = SearchResultDto(symbol = "  ", description = "Apple Inc").toInstrumentOrNull()
        assertNull(result)
    }

    @Test
    fun `toInstrumentOrNull prefers description for display name`() {
        val result = SearchResultDto(
            symbol = "AAPL",
            displaySymbol = "AAPL",
            description = "Apple Inc",
            type = "Common Stock",
        ).toInstrumentOrNull()

        assertEquals("AAPL", result?.symbol)
        assertEquals("Apple Inc", result?.displayName)
        assertEquals("Common Stock", result?.type)
    }

    @Test
    fun `toInstrumentOrNull falls back to display symbol when description is blank`() {
        val result = SearchResultDto(
            symbol = "AAPL",
            displaySymbol = "AAPL.US",
            description = "",
        ).toInstrumentOrNull()

        assertEquals("AAPL.US", result?.displayName)
    }

    @Test
    fun `toInstrumentOrNull falls back to symbol when description and display symbol are blank`() {
        val result = SearchResultDto(
            symbol = "AAPL",
            displaySymbol = null,
            description = null,
        ).toInstrumentOrNull()

        assertEquals("AAPL", result?.displayName)
    }

    @Test
    fun `toInstrumentOrNull uses empty type when type is null`() {
        val result = SearchResultDto(symbol = "AAPL", description = "Apple Inc", type = null)
            .toInstrumentOrNull()

        assertEquals("", result?.type)
    }

    @Test
    fun `toQuoteOrNull returns null for an all-zero payload`() {
        val result = QuoteDto(current = 0.0, previousClose = 0.0, timestamp = 0L)
            .toQuoteOrNull("AAPL")
        assertNull(result)
    }

    @Test
    fun `toQuoteOrNull maps a populated payload and converts seconds to millis`() {
        val result = QuoteDto(current = 195.5, previousClose = 199.11, timestamp = 1_700_000_000L)
            .toQuoteOrNull("AAPL")

        assertEquals("AAPL", result?.symbol)
        assertEquals(195.5, result?.price)
        assertEquals(199.11, result?.previousClose)
        assertEquals(1_700_000_000_000L, result?.updatedAt)
    }

    @Test
    fun `toQuoteOrNull treats a zero previous close as unknown`() {
        val result = QuoteDto(current = 195.5, previousClose = 0.0, timestamp = 1_700_000_000L)
            .toQuoteOrNull("AAPL")

        assertEquals(195.5, result?.price)
        assertNull(result?.previousClose)
    }

    @Test
    fun `toQuoteOrNull falls back to current time when timestamp is absent`() {
        val before = System.currentTimeMillis()
        val result = QuoteDto(current = 195.5, previousClose = 199.11, timestamp = 0L)
            .toQuoteOrNull("AAPL")
        val after = System.currentTimeMillis()

        val updatedAt = result?.updatedAt ?: error("expected a non-null quote")
        assert(updatedAt in before..after) { "updatedAt $updatedAt not within [$before, $after]" }
    }
}
