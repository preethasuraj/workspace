package com.example.test7.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.test7.R

@Composable
fun PropertyList1Screen(
    modifier: Modifier,
    vm: PropertyViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    PropertyList1ScreenWithState(modifier, uiState)
}

@Preview(showBackground = true)
@Composable
fun PropertyList1ScreenWithState(
    modifier: Modifier = Modifier.padding(2.dp),
    uiState: UiState = UiState.Loading,
) {
    when (uiState) {
        UiState.Loading -> LoadingScreen(modifier)
        is UiState.Success -> SuccessScreen1(modifier, uiState.properties)
    }
}

@Composable
fun SuccessScreen1(modifier: Modifier, uiEntities: List<UiEntity>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(3.dp)
    ) {
        LazyColumn() {
            items(
                items = uiEntities,
                key = { it.id }
            ) {
                PropertyRow1(it)

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PropertyRow1(
    uiEntity: UiEntity = UiEntity(
        id = "idd",
        name = "name",
        photUrl = "",
        price = "$$$$",
        rating = "5*"
    )
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true, properties = {})
            .padding(3.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = uiEntity.photUrl,
                contentDescription = "${uiEntity.name} image",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_launcher_background)
            )
            Spacer(
                modifier = Modifier
                    .padding(3.dp)
            )
            Column() {
                Text(
                    text = uiEntity.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (uiEntity.price != null) {
                    Text(
                        text = uiEntity.price,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (uiEntity.rating != null) {
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        color = Color.Yellow
                    ) {
                        Text(
                            text = uiEntity.rating,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}