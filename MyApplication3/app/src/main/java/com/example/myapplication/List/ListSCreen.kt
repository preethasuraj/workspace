package com.example.myapplication.List

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
fun ListScreen(
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel(),
    onRowClick: (String) -> Unit,

    ) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    var expandedId by remember { mutableStateOf<String?>(null) }
    val onExpand = remember {
        { id: String? ->
            expandedId = id
        }
    }
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
    ) { paddingValues ->
        when (val state = uiState) {
            is ListUiState.Success -> SuccessScreen(
                state,
                Modifier.padding(paddingValues),
                expandedId,
                onExpand,
                searchText,
                { id -> viewModel.updateSearch(id) },
                onRowClick,
            )

            ListUiState.Loading -> LoadingScreen(
                Modifier.padding(paddingValues)
            )
        }
    }


}

@Composable
fun SuccessScreen(
    listUiStateSuccess: ListUiState.Success,
    modifier: Modifier,
    expandedId: String?,
    onExpand: (String?) -> Unit,
    searchText: String,
    onSearch: (String) -> Unit,
    onRowClick: (String) -> Unit,
) {
    Column (
        modifier = modifier.padding(10.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { value -> onSearch(value)},
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = {onSearch("")}
                    )
                )
            }
        )

        Spacer(
            modifier = Modifier.size(10.dp)
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            items(
                items = listUiStateSuccess.list,
                key = { it.id }
            ) {
                HotelRow(it, expandedId, onExpand, onRowClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelRow(
    uiEntity: HotelUiEntity,
    expandedId: String?,
    onExpand: (String?) -> Unit,
    onRowClick: (String) -> Unit,

    ) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .semantics(mergeDescendants = true, {})
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp),
        onClick = {onRowClick(uiEntity.id)}
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = uiEntity.images[0],
                contentDescription = "${uiEntity.name} des",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit,
                onError = {},
                onLoading = {}
            )

            Column(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Text(
                    text = uiEntity.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                AnimatedVisibility(
                    visible = expandedId == uiEntity.id
                ) {
                    Column() {
                        Text(
                            text = uiEntity.description,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "",
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        onExpand(null)
                                    }
                                )
                        )
                    }
                }
                AnimatedVisibility(
                    visible = expandedId == null
                )
                {
                    Column() {
                        Text(
                            text = uiEntity.description,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "",
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        onExpand(uiEntity.id)
                                    }
                                )
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    CircularProgressIndicator()
}