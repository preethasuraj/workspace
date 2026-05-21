package com.example.hotels4.ui.details

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.hotels4.ui.list.HotelRow
import com.example.hotels4.ui.list.UiEntity
import com.example.hotels4.ui.list.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    uiState: com.example.hotels4.ui.details.UiState,
    onBack: () -> Unit,

) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hotels Details", // string resource
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier = modifier.clickable(onClick = {onBack()})
                    )
                }
            )

        }
    ) { paddingValues ->

        when (val state = uiState) {
            is com.example.hotels4.ui.details.UiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            com.example.hotels4.ui.details.UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            }

            is com.example.hotels4.ui.details.UiState.Details -> {
                val hotel = uiState.hotel
                Column (
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = uiState.hotel.images[0],
                        contentDescription = "image of the hotel ${hotel.name}",
                        placeholder = null,
                        error = null,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Column() {
                        Text(
                            text = hotel.name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = hotel.city,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = hotel.description,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }

}