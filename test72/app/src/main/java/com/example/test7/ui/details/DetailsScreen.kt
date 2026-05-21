package com.example.test7.ui.details

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(modifier: Modifier = Modifier, uiState: UiState, onBack: () -> Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details",
                        style = MaterialTheme.typography.headlineMedium
                    )

                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier= Modifier.clickable(onClick = {onBack()})
                    )
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            UiState.Loading -> {

            }

            is UiState.ShowingDetails -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(15.dp),
                    contentAlignment = Alignment.Center
                ){
                    Column() {
                        AsyncImage(
                            model = uiState.hotel.images[0],
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(200.dp)
                        )
                    }
                }
            }
        }
    }
}