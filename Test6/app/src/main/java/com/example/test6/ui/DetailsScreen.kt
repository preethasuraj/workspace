package com.example.test6.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.test6.R

@Composable
fun DetailsScreen(
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if(uiState is DetailsState.Success) {
        val entity = (uiState as DetailsState.Success).entity
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AsyncImage(
                    model = entity.photoUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(200.dp),
                    placeholder = painterResource(R.drawable.ic_android_black_24dp),
                    error = painterResource(R.drawable.ic_android_black_24dp),

                    )
                Spacer(
                    modifier = Modifier
                        .padding(5.dp)
                )
                Text(
                    text = entity.name,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        }
    }
}