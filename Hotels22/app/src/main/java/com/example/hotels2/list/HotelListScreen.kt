package com.example.hotels2.list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelListScreen(
    modifier: Modifier = Modifier,
    viewModel: HotelListViewModel = hiltViewModel(),
    onRowCLick: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hotel List",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        val state = uiState
        when {
            state.loading -> LoadingScreen(Modifier.padding(paddingValues))
            state.error != null -> ErrorScreen(Modifier.padding(paddingValues), state.error)
            !state.loading -> HotelsList(
                Modifier.padding(paddingValues),
                uiState.hotels,
                onRowCLick,
                uiState.searchText,
                { text: String -> viewModel.onIntent(UiIntent.Search(text)) },
                onExpand = { id: String -> viewModel.onIntent(UiIntent.ExpandHotel(id)) }
            )
        }
    }

}

@Composable
fun ErrorScreen(modifier: Modifier, error: String) {
    Box(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
        )
    }
}

@Composable
fun HotelsList(
    modifier: Modifier = Modifier,
    hotels: List<UiEntity>,
    onRowCLick: (String) -> Unit,
    searchText: String,
    onSearch: (String) -> Unit,
    onExpand: (String) -> Unit
) {
    var expandedId by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(5.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { value -> onSearch(value) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = { onSearch("") }
                    )
                )
            }
        )

        LazyColumn() {
            items(
                items = hotels,
                key = { it.id }
            ) {
                HotelRow(it, expandedId, onExpand, onRowCLick)
            }
        }
    }

}

@Composable
fun HotelRow(
    hotel: UiEntity,
    expandedId: String,
    onExpand: (String) -> Unit,
    onRowCLick: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .semantics(mergeDescendants = true, {}),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        onClick = { onRowCLick(hotel.id) }

    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = hotel.images.first(),
                contentDescription = "",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit,
                error = null,
                placeholder = null
            )
            Spacer(
                modifier = Modifier
                    .padding(15.dp)
            )
            Column(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Text(
                    text = hotel.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                val isExpanded = expandedId == hotel.id

                Column(modifier = Modifier.animateContentSize()) {
                    Text(
                        text = hotel.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    )

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else
                            Icons.Default.KeyboardArrowUp,
                        contentDescription = "TODO()",
                        modifier = Modifier
                            .clickable(onClick = {
                                onExpand(
                                    if (isExpanded) "" else
                                        hotel.id
                                )
                            })
                    )
                }
            }
        }
    }
}