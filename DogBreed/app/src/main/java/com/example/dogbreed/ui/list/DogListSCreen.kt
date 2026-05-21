package com.example.dogbreed.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogListScreen(
    modifier: Modifier = Modifier,
    viewModel: BreedListViewModel = hiltViewModel(),
    onRowClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dogs",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is UiState.Error -> {
                Box(
                    modifier = Modifier.padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            UiState.Loading -> {
                Box(
                    modifier = Modifier.padding(paddingValues),
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
                ) {
                    items(
                        items = state.uiEntity.dogBreeds,
                        key = { it }
                    ) {
                        DogRow(
                            state.uiEntity.dogSubBreeds[it]
                                ?: emptyList(),
                            it,
                            onRowClick

                        )
                    }
                }

            }
        }

    }

}

@Composable
fun DogRow(subBreeds: List<String>, breed: String, onRowClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        onClick =  {onRowClick(breed)},
        elevation = CardDefaults.elevatedCardElevation(3.dp),
    ) {
        Column(
        ) {
            Text(
                text = breed,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(5.dp)
            )
            subBreeds.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 15.dp)
                )
            }

        }
    }

}

