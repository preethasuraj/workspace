package com.example.hitels5.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.hitels5.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onRowClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hotels List", // todo string res
                        style = typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is UiState.Error -> {

                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        style = typography.headlineSmall
                    )
                }
            }

            UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.ShowingList -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(5.dp)
                ) {
                    items(
                        items = uiState.hotels,
                        key = { it.id }
                    ) {
                        HotelRow(it, onRowClick)
                    }
                }
            }

        }

    }

}

@Composable
fun HotelRow(hotel: UiEntity, onRowClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .semantics(true, {})
            .padding(10.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        onClick = {onRowClick(hotel.id)}
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = hotel.images[0],
                contentDescription = hotel.name,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.ic_launcher_background)
            )
            Spacer(
                modifier = Modifier.padding(10.dp)
            )
            Column() {
                Text(
                    text = hotel.name,
                    style = typography.headlineSmall
                )
                Text(
                    text = hotel.description,
                    style = typography.bodySmall
                )
            }
        }
    }
}