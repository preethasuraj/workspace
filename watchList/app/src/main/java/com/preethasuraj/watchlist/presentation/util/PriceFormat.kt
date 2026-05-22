package com.preethasuraj.watchlist.presentation.util

import java.util.Locale
import kotlin.math.abs

/** Price with currency, or a placeholder dash when unknown. Currency is USD (US stocks). */
fun formatPrice(price: Double?): String =
    if (price == null) "—" else String.format(Locale.US, "%,.2f USD", price)

/**
 * Day change line, e.g. "−3.61 (3.49%) today", or null when change can't be computed.
 * The signed amount conveys direction; the percent is shown as a magnitude.
 */
fun formatChange(change: Double?, percent: Double?): String? {
    if (change == null || percent == null) return null
    val sign = if (change >= 0) "+" else "−" // U+2212 minus sign
    return String.format(Locale.US, "%s%,.2f (%.2f%%) today", sign, abs(change), abs(percent))
}
