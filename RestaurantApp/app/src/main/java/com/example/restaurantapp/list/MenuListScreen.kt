package com.example.restaurantapp.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuListScreen(
    modifier: Modifier = Modifier,
    uiState: UiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Menu",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when (uiState) {
            UiState.Loading -> {
                Box(
                    modifier = modifier.padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.ShowingMenu -> {
                MenuScreen(uiState.categoryList, modifier)
            }
        }

    }
}

@Composable
fun MenuScreen(categories: List<UiCategory>, modifier: Modifier) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val categoriesMap = remember {
        buildMap {
            var index = 0
            categories.forEach { c ->
                put(c.id, index)
                index += 2
            }
        }
    }

    val selectedId by remember {
        derivedStateOf {
            val first = listState.firstVisibleItemIndex
            categories[first / 2].id
        }
    }
    val rowState = rememberLazyListState()
    LaunchedEffect(selectedId) {
        rowState.animateScrollToItem(categories.indexOfFirst { it.id == selectedId })
    }
    Column(
        modifier = modifier
            .padding(5.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .padding(5.dp),
            state = rowState
        ) {

            items(
                items = categories,
                key = { it.id }
            ) {
                FilterChip(
                    selected = it.id == selectedId,
                    onClick = {
                        scope.launch {

                            categoriesMap[it.id]?.let { index ->
                                listState.animateScrollToItem(index)
                            }
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
            modifier = Modifier.padding(10.dp)
        ) {
            categories.forEach { c ->
                stickyHeader(
                    key = "h-${c.id}",
                    content = {
                        Text(
                            text = c.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                )
                item {
                    LazyRow(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        items(
                            items = c.items,
                            key = { it.id }
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}