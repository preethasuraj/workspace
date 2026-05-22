package com.preethasuraj.watchlist.data.remote.ws

import com.preethasuraj.watchlist.BuildConfig
import com.preethasuraj.watchlist.di.ApplicationScope
import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.PricePoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

/**
 * Owns the single Finnhub trade WebSocket.
 *
 * The socket is opened on the first [subscribe] and closed when the last symbol is removed
 * via [unsubscribe]; it stays open while at least one symbol is watched (for the lifetime
 * of the app process). Transient drops reconnect with exponential backoff + jitter, and
 * the full desired-symbol set is re-subscribed on each (re)open since the server keeps no
 * memory across connections. Mutable state is guarded by [lock]; OkHttp callbacks arrive
 * on their own thread.
 */
@Singleton
class WebSocketManager @Inject constructor(
    client: OkHttpClient,
    private val json: Json,
    @ApplicationScope private val scope: CoroutineScope,
) {
    // Keep-alive ping prevents OkHttp's idle read timeout from closing a quiet stream;
    // the read timeout is disabled since a live socket is expected to idle between trades.
    private val wsClient: OkHttpClient = client.newBuilder()
        .pingInterval(PING_INTERVAL_SECONDS, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS)
        .build()

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _trades = MutableSharedFlow<PricePoint>(extraBufferCapacity = 128)
    val trades: SharedFlow<PricePoint> = _trades.asSharedFlow()

    private val lock = Any()
    private val desiredSymbols = mutableSetOf<String>()
    private var webSocket: WebSocket? = null
    private var reconnectJob: Job? = null
    private var reconnectAttempt = 0
    private var closedByUs = false

    fun subscribe(symbol: String) {
        synchronized(lock) {
            if (!desiredSymbols.add(symbol)) return
            val socket = webSocket
            if (socket == null) connect() else socket.send(subscribeFrame(symbol))
        }
    }

    fun unsubscribe(symbol: String) {
        synchronized(lock) {
            if (!desiredSymbols.remove(symbol)) return
            webSocket?.send(unsubscribeFrame(symbol))
            if (desiredSymbols.isEmpty()) close()
        }
    }

    /** Caller must hold [lock]. */
    private fun connect() {
        reconnectJob?.cancel()
        closedByUs = false
        _connectionState.value = ConnectionState.Connecting
        val request = Request.Builder()
            // Token in the URL; AuthInterceptor skips adding it when already present.
            .url("$WS_URL?token=${BuildConfig.FINNHUB_API_KEY}")
            .build()
        webSocket = wsClient.newWebSocket(request, listener)
    }

    /** Caller must hold [lock]. */
    private fun close() {
        closedByUs = true
        reconnectJob?.cancel()
        webSocket?.close(NORMAL_CLOSURE, null)
        webSocket = null
        reconnectAttempt = 0
        _connectionState.value = ConnectionState.Disconnected
    }

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            synchronized(lock) {
                reconnectAttempt = 0
                _connectionState.value = ConnectionState.Connected
                desiredSymbols.forEach { webSocket.send(subscribeFrame(it)) }
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val message = runCatching { json.decodeFromString<TradeMessage>(text) }.getOrNull()
            if (message?.type != "trade") return // ignore "ping"/error/unparseable frames
            message.data?.forEach {
                _trades.tryEmit(PricePoint(it.symbol, it.price, it.timestamp))
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            scheduleReconnect(rateLimited = response?.code == HTTP_TOO_MANY_REQUESTS)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            if (code != NORMAL_CLOSURE) scheduleReconnect()
        }
    }

    private fun scheduleReconnect(rateLimited: Boolean = false) {
        synchronized(lock) {
            if (closedByUs || desiredSymbols.isEmpty()) return
            webSocket = null
            val attempt = reconnectAttempt
            _connectionState.value = ConnectionState.Reconnecting(attempt + 1)
            reconnectJob?.cancel()
            reconnectJob = scope.launch {
                delay(backoffMs(attempt, rateLimited))
                synchronized(lock) {
                    if (!closedByUs && desiredSymbols.isNotEmpty()) {
                        reconnectAttempt = attempt + 1
                        connect()
                    }
                }
            }
        }
    }

    private fun backoffMs(attempt: Int, rateLimited: Boolean): Long {
        // 429 means Finnhub is throttling connection attempts (free tier is strict on
        // connection churn), so back off from a much higher floor to stop tripping it.
        val base = if (rateLimited) RATE_LIMIT_BACKOFF_MS else INITIAL_BACKOFF_MS
        val exponential = (base * 2.0.pow(attempt)).toLong()
        return min(exponential, MAX_BACKOFF_MS) + Random.nextLong(JITTER_MS)
    }

    private fun subscribeFrame(symbol: String) = """{"type":"subscribe","symbol":"$symbol"}"""
    private fun unsubscribeFrame(symbol: String) = """{"type":"unsubscribe","symbol":"$symbol"}"""

    private companion object {
        const val WS_URL = "wss://ws.finnhub.io"
        const val NORMAL_CLOSURE = 1000
        const val PING_INTERVAL_SECONDS = 20L
        const val INITIAL_BACKOFF_MS = 1_000L
        const val RATE_LIMIT_BACKOFF_MS = 15_000L
        const val MAX_BACKOFF_MS = 60_000L
        const val JITTER_MS = 1_000L
        const val HTTP_TOO_MANY_REQUESTS = 429
    }
}
