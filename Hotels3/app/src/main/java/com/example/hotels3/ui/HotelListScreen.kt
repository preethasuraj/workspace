package com.example.hotels3.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelListScreen(
    state: UiState,
    action: (Intent) -> Unit,
    onRowClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            Column() {
                TopAppBar(
                    title = {
                        Text(
                            text = "Hotels List",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },

                )
                OutlinedTextField(
                    value = (state).searchText ?: "",
                    onValueChange = { text -> action(Intent.Search(text)) },
                    modifier = Modifier.padding(5.dp).fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        when (state.listState) {
            is ListState.Error -> {

            }

            is ListState.ShowingList -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                ) {
                    items(
                        items = state.listState.hotels,
                        key = {it.id}
                    ) {
                        HotelRow(
                            it,
                            expandedId = state.listState.expandedId,
                            onAction = action,
                            onRowClick = onRowClick,
                        )
                    }

                }
            }

            ListState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
fun HotelRow(
    uiEntity: HotelUiEntity,
    expandedId: String?,
    onAction: (Intent) -> Unit,
    onRowClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .semantics(true, {}),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        onClick = {onRowClick(uiEntity.id)}

        ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.animateContentSize()
            ) {
                Text(
                    text = uiEntity.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = uiEntity.description,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = if (expandedId == uiEntity.id) Int.MAX_VALUE else 1
                )
                Icon(
                    imageVector = if (expandedId == uiEntity.id) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = null,
                    modifier = Modifier.clickable(onClick = {
                        onAction(
                            Intent.Expand(
                                uiEntity.id
                            )
                        )
                    })
                )
            }
        }
    }
}