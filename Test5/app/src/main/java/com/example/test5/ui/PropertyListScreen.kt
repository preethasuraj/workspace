package com.example.test5.ui

import android.R.attr.text
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.test5.R

@Composable
fun PropertyListScreen(
    modifier: Modifier,
    viewModel: PropertyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PropertyListScreenViewModelLess(uiState, modifier)
}

@Composable
fun PropertyListScreenViewModelLess(uiState: UiState, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp),

    ) {
        when (uiState) {
            UiState.Empty -> EmptyScreen()
            UiState.Error -> ErrorScreen()
            UiState.Loading -> LoadingScreen()
            is UiState.Success -> SuccessScreen(uiState.properties)
        }
    }

}

@Composable
fun SuccessScreen(properties: List<PropertyUiEntity>) {
    LazyColumn(
        contentPadding = PaddingValues(5.dp)
    ) {
        items(
            items = properties,
            key = { it.id }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics(mergeDescendants = true, properties = {})
                    .padding(3.dp),
                elevation = CardDefaults.elevatedCardElevation(3.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = it.url,
                        contentDescription = "${it.name} description",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                    )
                    Column(
                        modifier = Modifier
                            .padding(2.dp),
                    ) {

                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        if (it.price != null) {
                            Text(
                                text = it.price,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                        if (it.rating != null) {
                            Surface(
                                shape = RoundedCornerShape(2.dp),
                                color = Color.Yellow
                            ) {
                                Text(
                                    text = it.rating,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary,
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
fun LoadingScreen() {
    CircularProgressIndicator()
}

@Composable
fun ErrorScreen() {
    Text(
        text = stringResource(R.string.error),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.error,
    )
}

@Composable
fun EmptyScreen() {
    Text(
        text = stringResource(R.string.error),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary,
    )
}