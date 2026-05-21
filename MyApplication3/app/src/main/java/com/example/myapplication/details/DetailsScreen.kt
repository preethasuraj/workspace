package com.example.myapplication.details

import android.graphics.drawable.PaintDrawable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.myapplication.List.ListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),

    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details Screen",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is DetailsState.Details -> Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AsyncImage(
                    model = (uiState as DetailsState.Details).hotel.images.first(),
                    contentDescription = "TODO()",
                    modifier = Modifier
                        .size(250.dp),
//                        .clip(CircleShape),
                    contentScale = ContentScale.Fit,
                    error = null,
                    placeholder = null
                )
                Spacer(
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = (uiState as DetailsState.Details).hotel.name,
                    style = MaterialTheme.typography.headlineSmall
                )

            }

            DetailsState.Loading -> {

            }
        }

    }


}
