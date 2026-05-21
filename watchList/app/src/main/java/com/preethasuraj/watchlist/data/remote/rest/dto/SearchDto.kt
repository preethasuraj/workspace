package com.preethasuraj.watchlist.data.remote.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response of `GET /api/v1/search?q=...`.
 *
 * Fields default to safe empty values so a malformed or partial payload deserializes
 * without throwing. Combined with `Json { coerceInputValues = true }`, an explicit
 * `null` for a non-null field also falls back to the default.
 */
@Serializable
data class SearchResponseDto(
    @SerialName("count") val count: Int = 0,
    @SerialName("result") val result: List<SearchResultDto> = emptyList(),
)

@Serializable
data class SearchResultDto(
    @SerialName("symbol") val symbol: String? = null,
    @SerialName("displaySymbol") val displaySymbol: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("type") val type: String? = null,
)
