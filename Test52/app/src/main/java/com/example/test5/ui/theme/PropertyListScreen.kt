package com.example.test5.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage

@Composable
fun PropertyListScreen(
    modifier: Modifier,
    vm: PropertyViewModel = hiltViewModel()
) {

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    PropertyListScreenViewModelLess(
        uiState, modifier, { vm.getProperties() }
    )
}

@Preview()
@Composable
fun PropertyListScreenViewModelLess(
    uiState: UiState = UiState.Empty,
    modifier: Modifier = Modifier,
    refersh: () -> Unit = {}
) {
    when (uiState) {
        UiState.Empty -> EmptyScreen(modifier)
        UiState.Error -> ErrorScreen(modifier)
        UiState.Loading -> LoadingScreen(modifier)
        is UiState.Success -> SuccessScreen(modifier, uiState, refersh)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(modifier: Modifier, uiState: UiState.Success, refresh: () -> Unit) {
    PullToRefreshBox(
        isRefreshing = uiState == UiState.Loading,
        onRefresh = refresh,
        modifier = modifier
            .fillMaxWidth()
            .padding(3.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(
                items = uiState.properties,
                key = { it.id }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics(mergeDescendants = true, {})
                        .padding(3.dp),
                    elevation = CardDefaults.elevatedCardElevation(3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = it.photoUrl,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentDescription = "${it.name} description"
                        )
                        Spacer(
                            modifier = Modifier
                                .size(10.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(3.dp),
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            if (it.price != null) {
                                Text(
                                    text = it.price,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                            if (it.rating != null) {
                                Surface(
                                    shape = RoundedCornerShape(3.dp),
                                    color = Color.Yellow
                                ) {
                                    Text(
                                        text = it.rating,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
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

@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(x0: Modifier) {
    TODO("Not yet implemented")
}

@Composable
fun EmptyScreen(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Empty List",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}