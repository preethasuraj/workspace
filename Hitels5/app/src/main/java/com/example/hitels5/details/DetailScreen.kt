package com.example.hitels5.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
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
import com.example.hitels5.R
import com.example.hitels5.ui.list.UiEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(modifier: Modifier = Modifier, uiState: UiState, onBack: () -> Boolean, ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hotel Details", // todo string res
                        style = typography.headlineMedium
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier= Modifier.clickable(onClick = {onBack()})
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

            is UiState.ShowingDetails -> {
                Details(uiState.hotel)
            }

        }

    }

}

@Composable
fun Details(hotel: UiEntity ) {
    Column(
        modifier = Modifier.padding(15.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = hotel.images[0],
            contentDescription = hotel.name,
            modifier = Modifier.size(200.dp),
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