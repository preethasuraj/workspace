package com.example.test4.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.test4.R

@Composable
fun PropertyScreen(
    viewModel: PropertyViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()

    PropertyScreen1(uiState, searchText, modifier, {text: String -> viewModel.updateText(text)})
}

@Composable
fun PropertyScreen1(
    uiState: UiState,
    searchText: String,
    modifier: Modifier,
    function: (String) -> Unit
) {
    when (uiState) {
        UiState.Empty -> EmptyScreen(modifier)
        UiState.Error -> ErrorScreen(modifier)
        UiState.Loading -> LoadingScreen(modifier)
        is UiState.Success -> SuccessScreen(modifier,uiState.properties, searchText, function)
    }
}

@Composable
fun SuccessScreen(
    modifier: Modifier,
    properties: List<PropertyUiEntity>,
    searchText: String,
    function: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {function(it)},
                modifier = Modifier.padding(5.dp)
            )
            PropertyList(properties)
        }

    }
}

@Composable
fun PropertyList(properties: List<PropertyUiEntity>) {
    LazyColumn(contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)) {
        items(
            items = properties,
            key = { it.id }) {

            Card(Modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true, properties = {})
                .padding(5.dp),
                elevation = CardDefaults.elevatedCardElevation(3.dp),
                ){
                Row(
                    modifier = Modifier.padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    AsyncImage(
                        model = it.photUrl,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        contentDescription = "${it.name} details"
                    )
                    Spacer(Modifier
                        .size(8.dp))
                    Column(
                        modifier = Modifier
                            .padding(3.dp),
                        verticalArrangement = Arrangement.Center

                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        if (it.price != null) {
                            Text(
                                text = it.price,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        if (it.rating != null) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color.Yellow,
                            ) {
                                Text(
                                    text = it.rating,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }

                        }
                    }
                }
            }

        }

    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
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
fun ErrorScreen(modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.error),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun EmptyScreen(modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.empty),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
