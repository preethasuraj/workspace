package com.example.myapplication.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.OrdersNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: OrderViewModel,
    detailNav: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Orders Screen",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when (val state = uiState) {

            UiState.Loading -> {
                Box(
                    modifier = modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.ShowingOrders -> {
                PullToRefreshBox(
                    isRefreshing = uiState == UiState.Loading,
                    onRefresh = { },
                    modifier = modifier
                        .padding(10.dp)


                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        items(
                            items = (uiState as UiState.ShowingOrders).orders,
                            key = { it.id }
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .semantics(mergeDescendants = true, {}),
                                elevation = elevatedCardElevation(3.dp),
                                onClick = { detailNav(it.id) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.headlineSmall,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${it.count}",
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }

}