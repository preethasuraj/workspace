package com.preethasuraj.watchlist.data.remote

import kotlinx.coroutines.CancellationException

/**
 * Runs a suspending network call and wraps the outcome in a [Result], converting any
 * thrown exception into [Result.failure]. Coroutine cancellation is rethrown so that
 * structured concurrency is preserved (a canceled call must not look like a failure).
 */
suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
