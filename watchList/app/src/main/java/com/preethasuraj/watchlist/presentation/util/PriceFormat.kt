package com.preethasuraj.watchlist.presentation.util

import java.util.Locale

/** Formats a price for display, or a placeholder dash when no price is known yet. */
fun formatPrice(price: Double?): String =
    if (price == null) "—" else String.format(Locale.US, "$%,.2f", price)
