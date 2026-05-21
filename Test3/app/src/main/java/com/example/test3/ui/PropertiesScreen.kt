package com.example.test3.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.size.Scale
import com.example.test3.R

@Composable
fun PropertiesScreen(
    viewModel: PropertyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PropertiesScreen(uiState)
}

@Composable
fun PropertiesScreen(uiState: UiState) {
    when (uiState) {
        UiState.Empty -> EmptyScreen()
        UiState.Error -> ErrorScreen()
        UiState.Loading -> LoadingScreen()
        is UiState.Success -> ListScreen(uiState.properties)
    }

}

@Composable
fun ListScreen(properties: List<PropertyUiEntity>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.TopStart
    ) {
        LazyColumn(

        ) {
            items(
                items = properties,
                key = { it.id }
            ) {
                PropertyRow(it)
            }
        }
    }
}

@Composable
fun PropertyRow(proeprty: PropertyUiEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true, properties = {})
            .padding(5.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),

        ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = proeprty.photoUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "${proeprty.name} details",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(
                modifier = Modifier
                    .size(8.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
            ) {
                Text(
                    text = proeprty.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (proeprty.formattedPrice != null) {
                    Text(
                        text = proeprty.formattedPrice,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (proeprty.rating != null) {
                    RatingBadge(proeprty.rating)
                }


            }
        }

    }
}

@Composable
fun RatingBadge(rating: Float) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Color.Yellow,
        contentColor = Color.Black
    ) {
        Text(
            text = " $rating * ",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.error),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun EmptyScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.empty),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
