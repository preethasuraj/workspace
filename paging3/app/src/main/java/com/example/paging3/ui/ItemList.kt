package com.example.paging3.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemList(
    modifier: Modifier = Modifier,
    viewModel: ItemListViewModel = hiltViewModel(),
) {
    val items = viewModel.items.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "List",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            when {
                (items.loadState.refresh is LoadState.Loading) -> {
                    item { CircularProgressIndicator() }
                }

                (items.loadState.append is LoadState.Loading) -> {
                    item { CircularProgressIndicator() }
                }

                else -> {
                    items(
                        count = items.itemCount,
                        key = { index -> items[index]?.id ?: index }
                    ) { index ->
                        val item = items[index]
                        if (item != null) {

                            ItemRow(item)
                        }

                    }
                    if (items.loadState.refresh is LoadState.Error) {
                        item { Text("Error in loading items") }
                    }
                }
            }
        }
    }

}

@Composable
fun ItemRow(item: UiEntity) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .semantics(true, {}),
        elevation = CardDefaults.elevatedCardElevation(3.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = item.rating,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}