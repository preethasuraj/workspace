package com.example.myapplication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.w3c.dom.Text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelScreen(
    viewModel: HotelViewModel = hiltViewModel(),
    onRowClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hotels List",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        }
    ) { innerPadding ->
        HotelScreen(viewModel, Modifier.padding(innerPadding), onRowClick)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelScreen(
    viewModel: HotelViewModel,
    modifier: Modifier,
    onRowClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val searchText by viewModel.searchText.collectAsStateWithLifecycle()


    var expandedId by remember { mutableStateOf<String?>(null) }

    val onExpand = remember {
        { id: String ->
            expandedId = id

        }

    }

    when (val state = uiState) {
        UiState.Loading -> {
            Box(
                modifier = modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {
                CircularProgressIndicator(
                    modifier = modifier
                )
            }
        }

        is UiState.Success -> {
            Column(
                modifier = modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Something",
                    style = MaterialTheme.typography.headlineSmall
                )
                OutlinedTextField(
                    value = searchText,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    onValueChange = { input: String -> viewModel.onSearch(input) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.clickable(onClick = { viewModel.onSearch("") })
                        )
                    }
                )
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { },
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f)

                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        items(
                            items = state.hotels,
                            key = { it.id }
                        ) {
                            HotelRow(it, onRowClick, onExpand, expandedId)

                        }
                    }

                }

            }
        }
    }

}

@Composable
fun HotelRow(
    hotelUiEntity: HotelUiEntity,
    onRowClick: (String) -> Unit,
    onExpanded: (String) -> Unit,
    expandedId: String?,
) {
    val rotation by animateFloatAsState(
        if (expandedId == hotelUiEntity.id) 180f else 0f
    )
    Card(
        modifier = Modifier
            .padding(5.dp)
            .semantics(mergeDescendants = true, {})
            .animateContentSize()
            .fillMaxWidth(),
        onClick = { onRowClick(hotelUiEntity.id) },
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = hotelUiEntity.images.first(),
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(
                modifier = Modifier
                    .padding(10.dp)
            )
            Column(
                modifier = Modifier
                    .padding(5.dp),
            ) {
                Text(
                    text = hotelUiEntity.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = hotelUiEntity.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(rotation)
                        .clickable(onClick = { onExpanded(hotelUiEntity.id) })
                )

                if (expandedId == hotelUiEntity.id) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = hotelUiEntity.country,
                        style = MaterialTheme.typography.headlineSmall,

                        )
                }

            }

        }
    }
}



