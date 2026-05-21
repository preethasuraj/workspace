package com.example.test6.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.test6.R

@Composable
fun PropertyListScreen(
    modifier: Modifier,
    rowClick: (v: String) -> Unit,
    viewModel: PropertyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val refreshing by viewModel.refreshing.collectAsStateWithLifecycle()
    PropertyListScreen(
        modifier, uiState, refreshing, { viewModel.fetchProperties() }, rowClick
    )

}

@Composable
fun PropertyListScreen(
    modifier: Modifier,
    uiState: UiState,
    refreshing: Boolean,
    refresh: () -> Unit,
    rowClick: (String) -> Unit,
) {
    when (uiState) {
        UiState.Empty -> EmptyScreen(modifier)
        UiState.Error -> ErrorScreen(modifier)
        UiState.Loading -> LoadingScreen(modifier)
        is UiState.Success -> SuccessScreen(modifier, uiState, refreshing, refresh, rowClick)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(
    modifier: Modifier,
    uiState: UiState.Success,
    refreshing: Boolean,
    refresh: () -> Unit,
    rowClick: (String) -> Unit
) {
    PullToRefreshBox(
        modifier = modifier
            .fillMaxSize()
            .padding(3.dp),
        isRefreshing = refreshing,
        onRefresh = refresh,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(3.dp),
        ) {
            items(
                items = uiState.properties,
                key = { it.id }
            ) {
                PropertyRow(it, rowClick)
            }
        }

    }
}

@Composable
fun PropertyRow(property: UiEntity, rowClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true, properties = {})
            .padding(3.dp),
        onClick = {rowClick.invoke(property.id)},
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = property.photoUrl,
                contentDescription = "${property.name} row",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_android_black_24dp),
                error = painterResource(R.drawable.ic_android_black_24dp),
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
                    text = property.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (property.price != null) {
                    Text(
                        text = property.price,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                if (property.rating != null) {
                    Surface(
                        shape = RoundedCornerShape(2.dp),
                        color = Color.Yellow,
                    ) {
                        Text(
                            text = property.rating,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
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

}

@Composable
fun EmptyScreen(x0: Modifier) {

}