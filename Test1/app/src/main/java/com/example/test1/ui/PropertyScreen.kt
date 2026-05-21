package com.example.test1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.test1.R

@Composable
fun PropertyScreen(
    viewModel: PropertyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PropertyScreen(uiState)
}


@Composable
fun PropertyScreen(uiState: PropertyUiState) {
    when (uiState) {
        PropertyUiState.Empty -> EmptyScreen()
        is PropertyUiState.Error -> ErrorScreen(uiState.message)
        PropertyUiState.Loading -> LoadingScreen()
        is PropertyUiState.Success -> ListScreen(uiState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(uiState: PropertyUiState.Success) {
    PullToRefreshBox(
        isRefreshing = uiState == PropertyUiState.Loading,
        onRefresh = { },
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn() {
            items(
                items = uiState.properties,
                key = { it.uuid }
            ) {
                PropertyRow(it)
            }
        }
    }
}

@Composable
fun PropertyRow(property: PropertyUiEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = property.smallUrl,
                contentDescription = "${property.name} row ",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = property.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            }

        }
    }

    @Composable
    fun LoadingScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun ErrorScreen(message: String) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    @Composable
    fun EmptyScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.empty),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
