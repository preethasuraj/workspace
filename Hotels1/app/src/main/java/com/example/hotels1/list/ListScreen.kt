package com.example.hotels1.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel(),
    onRowClick: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hotels List",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            UiState.Loading -> LoadingScreen(Modifier.padding(padding))
            is UiState.Success -> HotelsList(
                Modifier.padding(padding), state.hotels, onRowClick
            )
        }

    }

}

@Composable
fun HotelsList(modifier: Modifier, hotels: List<HotelUiEntity>, onRowClick: (String) -> Unit) {
    var expandedId by remember { mutableStateOf("") }
    val onExpand = remember {
        { id: String ->
            expandedId = id
        }
    }
    LazyColumn(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize(),

        ) {
        items(
            items = hotels,
            key = { it.id }
        ) {
            HotelRow(it, expandedId, onExpand, onRowClick)
        }
    }
}

@Composable
fun HotelRow(
    entity: HotelUiEntity,
    expandedId: String,
    onExpand: (String) -> Unit,
    onRowClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .semantics(mergeDescendants = true, {}),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        onClick = { onRowClick(entity.id) }

    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = entity.images[0],
                contentDescription = null,
                error = null,
                placeholder = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = entity.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                AnimatedVisibility(expandedId != entity.id) {
                    Column() {
                        Text(
                            text = entity.description,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "TODO()",
                            modifier = Modifier.clickable(onClick = { onExpand(entity.id) })
                        )
                    }
                }
                AnimatedVisibility(expandedId == entity.id) {
                    Column() {
                        Text(
                            text = entity.description,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "TODO()",
                            modifier = Modifier.clickable(onClick = { onExpand("") })
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    TODO("Not yet implemented")
}
