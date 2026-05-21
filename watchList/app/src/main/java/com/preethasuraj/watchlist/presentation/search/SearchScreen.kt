package com.preethasuraj.watchlist.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.presentation.common.EmptyState
import com.preethasuraj.watchlist.presentation.common.ErrorState
import com.preethasuraj.watchlist.presentation.common.LoadingState

@Composable
fun SearchScreen(
    onBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreenContent(
        state = state,
        onBack = onBack,
        onQueryChange = viewModel::onQueryChange,
        onAdd = viewModel::add,
        onRemove = viewModel::remove,
        onRetry = viewModel::onRetry,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContent(
    state: SearchUiState,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onAdd: (Instrument) -> Unit,
    onRemove: (String) -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                label = { Text("Search stocks (e.g. AAPL)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isSearching -> LoadingState()
                    state.error != null -> ErrorState(message = state.error, onRetry = onRetry)
                    state.results.isEmpty() && state.query.isBlank() ->
                        EmptyState("Search for a stock symbol or company name.")
                    state.results.isEmpty() ->
                        EmptyState("No matches for \"${state.query}\".")
                    else -> SearchResultsList(
                        results = state.results,
                        onAdd = onAdd,
                        onRemove = onRemove,
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultsList(
    results: List<SearchResultUi>,
    onAdd: (Instrument) -> Unit,
    onRemove: (String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(results, key = { it.instrument.symbol }) { row ->
            ListItem(
                headlineContent = { Text(row.instrument.symbol) },
                supportingContent = { Text(row.instrument.displayName) },
                trailingContent = {
                    if (row.isWatched) {
                        TextButton(onClick = { onRemove(row.instrument.symbol) }) {
                            Text("Remove")
                        }
                    } else {
                        TextButton(onClick = { onAdd(row.instrument) }) {
                            Text("Add")
                        }
                    }
                },
            )
            HorizontalDivider()
        }
    }
}
