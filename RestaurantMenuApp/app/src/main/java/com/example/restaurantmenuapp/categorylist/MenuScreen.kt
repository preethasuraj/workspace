package com.example.restaurantmenuapp.categorylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    uiState: UiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "$$",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when (uiState) {
            is UiState.Error -> {
                Box(
                    modifier = modifier
                        .padding(15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            UiState.Loading -> {
                Box(
                    modifier = modifier
                        .padding(15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.ShowingList -> {
                ListScreen(
                    uiState.uiCategories,
                    modifier
                )
            }
        }

    }

}

@Composable
fun ListScreen(categories: List<UiCategory>, modifier: Modifier) {
    val scoped = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val categoryMap = remember(categories) {
        buildMap {
            var idx = 0
            categories.forEach {
                put(it.id, idx)
                idx += 2
            }
        }
    }
    val selectedCategoryId  by remember {
        derivedStateOf {
            val first = listState.firstVisibleItemIndex
            categories[first / 2].id
        }
    }

    val rowState = rememberLazyListState()

    LaunchedEffect(selectedCategoryId) {
            rowState.animateScrollToItem(
                categories.indexOfFirst { it.id == selectedCategoryId })
    }
    Column(
        modifier = modifier
            .padding(15.dp)
    ) {
        LazyRow(
            state = rowState,
            modifier = Modifier
                .padding(5.dp)
        ) {
            items(
                items = categories,
                key = { it.id }
            ) {
                FilterChip(
                    selected = it.id == selectedCategoryId,
                    onClick = {
                        scoped.launch {
                            categoryMap[it.id]?.let { index -> listState.animateScrollToItem(index) }
                        }
                    },
                    label = {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                )
            }
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(15.dp)
        ) {
            categories.forEach { c ->
                stickyHeader(
                    key = "h-${c.id}",
                    content = {
                        Surface(
                            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = c.name,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .padding(5.dp)
                            )
                        }
                    }
                )
                item {
                    LazyRow() {
                        items(
                            items = c.items,
                            key = { it.id }
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(5.dp),
                                elevation = CardDefaults.elevatedCardElevation(3.dp)
                            ) {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier
                                        .padding(5.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryRow(menuItems: List<UiItem>) {

    LazyRow(
        modifier = Modifier
            .padding(15.dp)
    ) {
        items(
            items = menuItems,
            key = { it.id }
        ) {
            Card(
                modifier = Modifier
                    .padding(5.dp),
                elevation = CardDefaults.elevatedCardElevation(3.dp)
            ) {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(5.dp),
                )
            }
        }

    }

}