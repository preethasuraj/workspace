package com.preethasuraj.watchlist.presentation.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.preethasuraj.watchlist.R
import com.preethasuraj.watchlist.domain.model.ConnectionState
import com.preethasuraj.watchlist.domain.model.PriceMovement
import com.preethasuraj.watchlist.domain.model.WatchlistItem
import com.preethasuraj.watchlist.presentation.common.EmptyState
import com.preethasuraj.watchlist.presentation.common.LoadingState
import com.preethasuraj.watchlist.presentation.util.formatChange
import com.preethasuraj.watchlist.presentation.util.formatPrice

@Composable
fun WatchlistScreen(
    onSearch: () -> Unit,
    viewModel: WatchlistViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    WatchlistScreenContent(
        state = state,
        onSearch = onSearch,
        onRemove = viewModel::remove,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchlistScreenContent(
    state: WatchlistUiState,
    onSearch: () -> Unit,
    onRemove: (String) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.watchlist_title)) }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onSearch) {
                Text(stringResource(R.string.action_search))
            }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingState()
                state.items.isEmpty() ->
                    EmptyState(stringResource(R.string.watchlist_empty))
                else -> Column(modifier = Modifier.fillMaxSize()) {
                    ConnectionBanner(state.connection)
                    WatchlistList(items = state.items, onRemove = onRemove)
                }
            }
        }
    }
}

@Composable
private fun ConnectionBanner(connection: ConnectionState) {
    if (connection is ConnectionState.Connected) return
    // Connectivity is inferred from the socket, not the OS. A genuine network drop surfaces
    // as Reconnecting (the retry loop keeps running); Disconnected only appears as a brief
    // startup blip before the socket opens. Both mean the live feed is unavailable and rows
    // are showing last-known (stale) prices, so they share the reconnect message. The retry
    // count is intentionally omitted — it's noise to the user and can grow large offline.
    val message = when (connection) {
        ConnectionState.Connecting -> stringResource(R.string.connection_connecting)
        else -> stringResource(R.string.connection_reconnecting)
    }
    val container = when (connection) {
        ConnectionState.Connecting -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.errorContainer
    }
    Surface(color = container, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun WatchlistList(
    items: List<WatchlistItem>,
    onRemove: (String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items, key = { it.instrument.symbol }) { item ->
            WatchlistRow(item = item, onRemove = onRemove)
            HorizontalDivider()
        }
    }
}

@Composable
private fun WatchlistRow(item: WatchlistItem, onRemove: (String) -> Unit) {
    val up = Color(0xFF2E7D32)
    val down = Color(0xFFC62828)
    val changeColor = when (item.movement) {
        PriceMovement.UP -> up
        PriceMovement.DOWN -> down
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val alpha = if (item.isStale) 0.45f else 1f
    val supporting = if (item.isStale) {
        stringResource(R.string.row_stale, item.instrument.displayName)
    } else {
        item.instrument.displayName
    }
    val changeText = formatChange(item.change, item.changePercent)

    ListItem(
        headlineContent = { Text(item.instrument.symbol) },
        supportingContent = { Text(supporting) },
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatPrice(item.price),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                    )
                    if (changeText != null) {
                        Text(
                            text = changeText,
                            style = MaterialTheme.typography.bodySmall,
                            color = changeColor.copy(alpha = alpha),
                        )
                    }
                }
                TextButton(onClick = { onRemove(item.instrument.symbol) }) {
                    Text(stringResource(R.string.action_remove))
                }
            }
        },
    )
}
