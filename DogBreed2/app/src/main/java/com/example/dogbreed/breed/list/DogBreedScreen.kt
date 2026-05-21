package com.example.dogbreed.breed.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogBreedScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onBreedSelect: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Breed/subbreed list",
                        style = typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when (uiState) {
            is UiState.Error -> {
                Box(
                    modifier = modifier
                        .padding(5.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        text = uiState.message,
                        style = typography.headlineSmall
                    )
                }
            }

            UiState.Loading -> {
                Box(
                    modifier = modifier
                        .padding(5.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                )
                {
                    CircularProgressIndicator()
                }
            }

            is UiState.ShowingList -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    items(
                        items = uiState.dogBreedToSubbreedMap.dogBreed.keys.toList(),
                        key = { it }
                    ) {
                        BreedRow(
                            it,
                            uiState.dogBreedToSubbreedMap.dogBreed[it],
                            onBreedSelect

                        )
                    }
                }
            }
        }

    }

}

@Composable
fun BreedRow(breed: String, subBreed: List<String>?, onSelect: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Column() {
            Text(
                text = breed,
                style = typography.headlineSmall,
            )

            LazyRow(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                items(
                    items = subBreed ?: emptyList(),
                    key = { it },
                ) {
                    Text(
                        text = it,
                        style = typography.bodyMedium,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }
            }
        }
    }
}