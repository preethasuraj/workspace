package com.example.dogbreed.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.dogbreed.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    uiState: com.example.dogbreed.ui.details.UiState,
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dogs",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier.clickable(onClick = {onBack()})
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


            is UiState.ShowingDetails -> {

                DogDetails(
                    state.uiEntity,
                    modifier = Modifier.padding(paddingValues)
                )


            }
        }

    }

}

@Composable
fun DogDetails(details: DetailsUiEntity, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        LazyRow(
            modifier = Modifier
                .padding(15.dp)
        ) {
            items(
                items = details.images,
                key = { it }
            ) {
                AsyncImage(
                    model = it,
                    contentDescription = "",
                    modifier = Modifier.size(200.dp).padding(10.dp),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.ic_launcher_background)

                )
            }

        }
        Spacer(
            modifier = Modifier
                .padding(15.dp)
        )
        Text(
            text = details.breed,
            style = MaterialTheme.typography.headlineSmall
        )
        details.subbreed.forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 15.dp)
            )
        }


    }
}