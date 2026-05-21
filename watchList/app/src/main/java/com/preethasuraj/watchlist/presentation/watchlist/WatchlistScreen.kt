package com.preethasuraj.watchlist.presentation.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.preethasuraj.watchlist.domain.model.WatchedInstrument
import com.preethasuraj.watchlist.presentation.common.EmptyState
import com.preethasuraj.watchlist.presentation.common.LoadingState
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
        topBar = { TopAppBar(title = { Text("Watchlist") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onSearch) {
                Text("Search")
            }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingState()
                state.items.isEmpty() ->
                    EmptyState("Your watchlist is empty.\nTap Search to add instruments.")
                else -> WatchlistList(items = state.items, onRemove = onRemove)
            }
        }
    }
}

@Composable
private fun WatchlistList(
    items: List<WatchedInstrument>,
    onRemove: (String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items, key = { it.instrument.symbol }) { item ->
            ListItem(
                headlineContent = { Text(item.instrument.symbol) },
                supportingContent = { Text(item.instrument.displayName) },
                trailingContent = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = formatPrice(item.lastPrice),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        TextButton(onClick = { onRemove(item.instrument.symbol) }) {
                            Text("Remove")
                        }
                    }
                },
            )
            HorizontalDivider()
        }
    }
}
