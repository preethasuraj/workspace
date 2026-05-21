package com.example.test7.ui.list

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.test7.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelListScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onRowClick: (String) -> Unit,
    onAction: (Intent) -> Unit
) {
    Scaffold(
        topBar = {
            Column() {
                TopAppBar(
                    title = {
                        Text(
                            text = "Hotel List",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                )
                OutlinedTextField(
                    value = (uiState as? UiState.ShowingList)?.searchText ?: "",
                    onValueChange = { value: String -> onAction(Intent.Search(value)) }
                )
            }
        }
    ) { paddingValues ->

        when (uiState) {
            is UiState.Error -> {

                Box(
                    modifier = Modifier
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.ShowingList -> {

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(10.dp)
                        .fillMaxSize()

                ) {
                    items(
                        items = uiState.hotels,
                        key = { it.id }) {
                        HotelRow(
                            it,
                            onRowClick,
                            onAction,
                            uiState.expandedId
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun HotelRow(uiEntity: UiEntity, onRowClick: (String) -> Unit,
             onAction: (Intent) -> Unit,
             expandedId: String?) {
    Card(
        modifier = Modifier
            .animateContentSize()
            .padding(5.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        onClick = {onRowClick(uiEntity.id)},


        ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = uiEntity.images[0],
                contentDescription = "",
                contentScale = ContentScale.Fit,
                error = painterResource(R.drawable.ic_launcher_background),
                modifier = Modifier
                    .size(90.dp)
            )
            Spacer(
                modifier = Modifier
                    .padding(15.dp)
            )
            Column(
                modifier = Modifier
                    .padding(5.dp),

                ) {
                Text(
                    text = uiEntity.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = uiEntity.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = if(uiEntity.id != expandedId) 1 else Int.MAX_VALUE
                )
                Icon(
                    imageVector = if (expandedId == uiEntity.id) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = {onAction(Intent.Expand(uiEntity.id))}
                    )
                )
            }

        }

    }
}