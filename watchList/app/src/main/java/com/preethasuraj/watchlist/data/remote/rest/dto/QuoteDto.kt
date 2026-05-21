package com.preethasuraj.watchlist.data.remote.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response of `GET /api/v1/quote?symbol=...`, narrowed to the fields the app consumes.
 * Other fields Finnhub returns (`d`, `dp`, `h`, `l`, `o`) are ignored by the parser
 * (`Json { ignoreUnknownKeys = true }`).
 *
 * Finnhub returns an all-zero payload for unknown symbols or when no data is available,
 * which the mapper treats as "no quote" rather than a price of 0.
 *
 * @param current current price (`c`).
 * @param previousClose previous close (`pc`).
 * @param timestamp quote time in epoch **seconds** (`t`).
 */
@Serializable
data class QuoteDto(
    @SerialName("c") val current: Double = 0.0,
    @SerialName("pc") val previousClose: Double = 0.0,
    @SerialName("t") val timestamp: Long = 0L,
)
